package org.temperature.model;

public record TemperatureMeasurement(Long timestampMs, Double temperature, String thermometerIdentifier) {

}
