package org.temperature.model;

import java.util.Objects;

public class TemperatureMeasurement {
    private final Long timestampMs;
    private final Double temperature;

    public TemperatureMeasurement(Long timestampMs, Double temperature) {
        this.timestampMs = Objects.requireNonNull(timestampMs);
        this.temperature = Objects.requireNonNull(temperature);
    }

    public Double getTemperature() {
        return temperature;
    }

    public Long getTimestampMs() {
        return timestampMs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureMeasurement that = (TemperatureMeasurement) o;
        return Objects.equals(timestampMs, that.timestampMs) && Objects.equals(temperature, that.temperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestampMs, temperature);
    }

    @Override
    public String toString() {
        return "TemperatureMeasurement{" +
                "timestampMs=" + timestampMs +
                ", temperature=" + temperature +
                '}';
    }
}
