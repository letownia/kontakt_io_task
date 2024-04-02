package org.temperature.anomalies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.temperature.model.TemperatureMeasurement;

import java.util.*;
import java.util.stream.Collectors;
import org.temperature.model.db.Temperature;
import org.temperature.repository.TemperatureRepository;


public class TimeAgnosticAlgorithm implements AnomalyDetectionAlgorithm {

  private static final Logger log = LoggerFactory.getLogger(TimeAgnosticAlgorithm.class);

  @Autowired
  private TemperatureRepository temperatureRepository;
  static final double OUTLIER_THRESHOLD_TEMPERATURE = 5.0;

  private final int windowSize;
  public TimeAgnosticAlgorithm(int windowSize) {
    this.windowSize = windowSize;
  }
  @Override @Deprecated
  public Set<TemperatureMeasurement> findAllAnomalies(
      List<TemperatureMeasurement> temperatureMeasurements) {
    if (temperatureMeasurements.size() < windowSize) {
      log.warn("No anomalies can be detected with less than " + windowSize + " measurements");
      return Collections.EMPTY_SET;
    }
    Set<TemperatureMeasurement> foundAnomalies = new HashSet<>();

    for (int i = 0; i <= temperatureMeasurements.size() - windowSize; i++) {
      double averageTemp = temperatureMeasurements.stream().skip(i).limit(windowSize)
          .mapToDouble(x -> x.temperature()).average().getAsDouble();
      Set<TemperatureMeasurement> newAnomalies =
          temperatureMeasurements.stream().skip(i).limit(windowSize)
              .filter(x -> Math.abs(x.temperature() - averageTemp) >= OUTLIER_THRESHOLD_TEMPERATURE)
              .collect(Collectors.toSet());
      log.info("Found " + newAnomalies.size() + " new anomalies");
      foundAnomalies.addAll(newAnomalies);
    }
    log.info("In total found" + foundAnomalies.size()+ " anomalies :" + foundAnomalies);
    return foundAnomalies;
  }

  @Override
  public List<Temperature> findNewAnomaliesForNewTemperature(Temperature temperature) {
    List<Temperature> xPreviousTemperatures = temperatureRepository
        .getXPreviousTemperatures(windowSize, temperature.getId(), temperature.getThermometer().getId());

    if (xPreviousTemperatures.size() < windowSize) {
      log.warn("No anomalies can be detected with less than " + windowSize + " measurements");
      return Collections.EMPTY_LIST;
    }

    double averageTemp = xPreviousTemperatures.stream().mapToDouble(x -> x.getTemperature()).average().getAsDouble();

    Set<Temperature> newAnomalies = xPreviousTemperatures.stream()
        .filter( x -> x.isTimeAgnosticAnomaly() == false)
        .filter(x -> Math.abs(x.getTemperature() - averageTemp) >= OUTLIER_THRESHOLD_TEMPERATURE)
            .collect(Collectors.toSet());
    return new ArrayList<>(newAnomalies);
  }
}
