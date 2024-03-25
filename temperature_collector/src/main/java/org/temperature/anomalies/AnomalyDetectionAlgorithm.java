package org.temperature.anomalies;

import org.temperature.model.TemperatureMeasurement;

import java.util.List;
import java.util.Set;

public interface AnomalyDetectionAlgorithm {

  Set<TemperatureMeasurement> findAllAnomalies(
      List<TemperatureMeasurement> temperatureMeasurements);
}
