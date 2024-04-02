package org.temperature.anomalies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.temperature.model.TemperatureMeasurement;

@Component
public class KafkaAnomalyEventProducer {
//  @KafkaListener(id = "1", topics = "${temperature.topic.name}")
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void newTimeAgnosticAnomaly(TemperatureMeasurement measurement) {
    kafkaTemplate.send("test", measurement.toString());
  }

  public void newTimeSensitiveAnomaly(TemperatureMeasurement measurement) {
    kafkaTemplate.send("test", measurement.toString());
  }
}
