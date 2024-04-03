package org.temperature.anomalies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.temperature.model.AnomalyType;
import org.temperature.model.TemperatureMeasurement;

@Component
public class KafkaAnomalyEventProducer {
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void newTimeAgnosticAnomaly(TemperatureMeasurement measurement) {
    kafkaTemplate.send("anomalies", measurement.toAnomaly(AnomalyType.TIME_AGNOSTIC).toString());
  }

  public void newTimeSensitiveAnomaly(TemperatureMeasurement measurement) {
    kafkaTemplate.send("anomalies", measurement.toAnomaly(AnomalyType.TIME_SENSITIVE).toString());
  }
}
