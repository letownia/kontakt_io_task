package org.temperature.anomalies;

import org.temperature.model.TemperatureMeasurement;

import java.util.List;
import java.util.Set;
import org.temperature.model.db.Temperature;

public interface AnomalyDetectionAlgorithm {

  Set<TemperatureMeasurement> findAllAnomalies(
      List<TemperatureMeasurement> temperatureMeasurements);

  List<Temperature> findNewAnomaliesForNewTemperature(Temperature temperature);
}
