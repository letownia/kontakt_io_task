package org.temperature.anomalies;

import org.temperature.model.TemperatureMeasurement;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TimeSensitiveAlgorithm implements AnomalyDetectionAlgorithm {

  private final int windowSizeInSeconds;

  public TimeSensitiveAlgorithm(int windowSizeInSeconds) {
    this.windowSizeInSeconds = windowSizeInSeconds;
  }

  @Override
  public Set<TemperatureMeasurement> findAllAnomalies(
      List<TemperatureMeasurement> temperatureMeasurements) {
    Set<TemperatureMeasurement> foundAnomalies = new HashSet<>();
    List<TemperatureMeasurement> sortedTemperatures = temperatureMeasurements.stream()
        .sorted(Comparator.comparingLong(TemperatureMeasurement::timestampMs))
        .collect(Collectors.toList());
    return null;
  }
}
