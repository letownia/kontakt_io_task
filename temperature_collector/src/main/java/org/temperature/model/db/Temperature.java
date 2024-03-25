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
}
