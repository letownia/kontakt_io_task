package org.temperature.model.db;

import jakarta.persistence.*;

@Entity
public class Temperature {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "thermometer_id")
  private Thermometer thermometer;

  private boolean timeSensitiveAnomaly;
  private boolean timeAgnosticAnomaly;
  private Long timestampMs;
  private Double temperature;

  public Thermometer getThermometer() {
    return thermometer;
  }

  public long getId() {
    return id;
  }

  public Long getTimestampMs() {
    return timestampMs;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTimestampMs(Long timestampMs) {
    this.timestampMs = timestampMs;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public void setThermometer(Thermometer thermometer) {
    this.thermometer = thermometer;
  }

  public boolean isTimeSensitiveAnomaly() {
    return timeSensitiveAnomaly;
  }

  public void setTimeSensitiveAnomaly(boolean timeSensitiveAnomaly) {
    this.timeSensitiveAnomaly = timeSensitiveAnomaly;
  }

  public boolean isTimeAgnosticAnomaly() {
    return timeAgnosticAnomaly;
  }

  public void setTimeAgnosticAnomaly(boolean timeAgnosticAnomaly) {
    this.timeAgnosticAnomaly = timeAgnosticAnomaly;
  }
}
