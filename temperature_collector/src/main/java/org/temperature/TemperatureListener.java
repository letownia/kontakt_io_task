package org.temperature;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.temperature.anomalies.KafkaAnomalyEventProducer;
import org.temperature.anomalies.TimeAgnosticAlgorithm;
import org.temperature.anomalies.TimeSensitiveAlgorithm;
import org.temperature.model.TemperatureMeasurement;
import org.temperature.model.db.Temperature;
import org.temperature.model.db.Thermometer;
import org.temperature.repository.TemperatureRepository;
import org.temperature.repository.ThermometerRepository;

@Component
public class TemperatureListener {

  @Autowired
  private TimeAgnosticAlgorithm timeAgnosticAlgorithm;
  @Autowired
  private TimeSensitiveAlgorithm timeSensitiveAlgorithm;

  @Autowired
  private KafkaAnomalyEventProducer kafkaAnomalyEventProducer;
  @Autowired
  private TemperatureRepository temperatureRepository;

  @Autowired
  private ThermometerRepository thermometerRepository;
  private static final Logger log = LoggerFactory.getLogger(TemperatureListener.class);

  @KafkaListener(id = "1", topics = "${temperature.topic.name}")
  public void listen(TemperatureMeasurement measurement) {
    log.info("Received TemperatureMeasurement " + measurement);
    Thermometer thermometer = thermometerRepository.findByIdentifier(measurement.thermometerIdentifier());
    Temperature temperature = new Temperature();
    temperature.setTemperature(measurement.temperature());
    temperature.setTimestampMs(measurement.timestampMs());
    temperature.setThermometer(thermometer);

    temperatureRepository.save(temperature);

//    anomalyWatcher.detectAnomalies(temperature);


    List<Temperature> newTimeAgnosticAnomalies = timeAgnosticAlgorithm.findNewAnomaliesForNewTemperature(temperature);
    newTimeAgnosticAnomalies.forEach( x-> x.setTimeAgnosticAnomaly(true));
    temperatureRepository.saveAll(newTimeAgnosticAnomalies);

    List<Temperature> newTimeSensitiveAnomalies = timeSensitiveAlgorithm.findNewAnomaliesForNewTemperature(temperature);
    newTimeSensitiveAnomalies.forEach( x-> x.setTimeSensitiveAnomaly(true));
    temperatureRepository.saveAll(newTimeSensitiveAnomalies);

    newTimeAgnosticAnomalies.forEach( x -> kafkaAnomalyEventProducer.newTimeAgnosticAnomaly(new TemperatureMeasurement(x.getTimestampMs(),x.getTemperature(), x.getThermometer().getIdentifier())));
  }
}
