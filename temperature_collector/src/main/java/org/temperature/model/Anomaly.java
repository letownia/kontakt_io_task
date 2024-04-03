package org.temperature.model;

public record Anomaly(Long timestampMs, Double temperature, String thermometerIdentifier, AnomalyType type) {

}
