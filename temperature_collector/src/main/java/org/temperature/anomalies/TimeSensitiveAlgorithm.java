package org.temperature.anomalies;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.temperature.model.TemperatureMeasurement;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.temperature.model.db.Temperature;
import org.temperature.repository.TemperatureRepository;


public class TimeSensitiveAlgorithm implements AnomalyDetectionAlgorithm {

  private static final Logger log = LoggerFactory.getLogger(TimeSensitiveAlgorithm.class);

  static final double OUTLIER_THRESHOLD_TEMPERATURE = 5.0;

  @Autowired
  private TemperatureRepository temperatureRepository;
  private final int windowSizeMs;

  public TimeSensitiveAlgorithm(int windowSizeMs) {
    this.windowSizeMs = windowSizeMs;
  }

  @Override   @Deprecated
  public Set<TemperatureMeasurement> findAllAnomalies(
      List<TemperatureMeasurement> unsortedTemperatures) {
    Set<TemperatureMeasurement> foundAnomalies = new HashSet<>();

    if (unsortedTemperatures.size() < 2) {
      log.warn("At least 2 measurements are required in order to determine time-sensitive anomalies");
      return Collections.EMPTY_SET;
    }
    List<TemperatureMeasurement> sortedTemperatures = unsortedTemperatures.stream()
        .sorted(Comparator.comparingLong(TemperatureMeasurement::timestampMs))
        .collect(Collectors.toList());
    int previousWindowEnd = -1;
    for(int start = 0; start < sortedTemperatures.size()-1; start++) {
      // Step 1: determine "start" and "end" of window to be analyzed
      long startTimeMs = sortedTemperatures.get(start).timestampMs();
      int end = -1;
      for(int i = Math.max(previousWindowEnd, start);
          i < sortedTemperatures.size() && sortedTemperatures.get(i).timestampMs() <= startTimeMs + windowSizeMs;
          i++) {
        end = i;
      }
      if(end == -1) {
        log.warn("Isolated temperature measurement without any nearby measurements");
        continue;
      }
      if(end == previousWindowEnd) {
        //Already analyzed this window - continue
        continue;
      }
      log.info("Analyzing window : from :" + start + " to : " + end);
      int numElements =  end - start + 1;

      //Step 2: Calculate average from start to end
      double averageTemp = sortedTemperatures.stream().skip(start).limit(numElements).mapToDouble(x -> x.temperature()).average().getAsDouble();

      //Step 3: Determine outliers from average and add them to set of found anomalies
      Set<TemperatureMeasurement> newAnomalies = sortedTemperatures.stream().skip(start).limit(numElements).filter(x -> Math.abs(x.temperature() - averageTemp) >= OUTLIER_THRESHOLD_TEMPERATURE).collect(
          Collectors.toSet());
      foundAnomalies.addAll(newAnomalies);
      //Optimization - no need to test for new "end" positions, that are less than the previous "end" position
      previousWindowEnd = end;
      //Edge-case - once end has reached end of list, we might as well break.
      if(end == sortedTemperatures.size()-1 ) {
        break;
      }
    }

    log.info("In total found" + foundAnomalies.size()+ " anomalies :" + foundAnomalies);

    return foundAnomalies;
  }

  @Override
  public List<Temperature> findNewAnomaliesForNewTemperature(Temperature temperature) {
    List<Temperature> temperatures = temperatureRepository
        .getTemperaturesInRange(temperature.getTimestampMs()-1000*10, temperature.getTimestampMs(), temperature.getThermometer().getId());
    double averageTemp = temperatures.stream().mapToDouble(x -> x.getTemperature()).average().getAsDouble();

    return temperatures.stream()
        .filter( x -> x.isTimeSensitiveAnomaly() == false)
        .filter(x -> Math.abs(x.getTemperature() - averageTemp) >= OUTLIER_THRESHOLD_TEMPERATURE).collect(
        Collectors.toList());
  }
}
