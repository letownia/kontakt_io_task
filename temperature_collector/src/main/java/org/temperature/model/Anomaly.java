package org.temperature.model;

public record Anomaly(Long timestampMs, Double temperature, Long thermometerIdentifier, AnomalyType type) {

}
