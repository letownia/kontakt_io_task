package org.temperature.model;

public record TemperatureMeasurement(Long timestampMs, Double temperature, String thermometerIdentifier) {
  public Anomaly toAnomaly(AnomalyType type) {
    return new Anomaly(timestampMs, temperature, thermometerIdentifier, type);
  }
}
