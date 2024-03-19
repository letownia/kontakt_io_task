package org.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.temperature.model.TemperatureMeasurement;
import org.temperature.model.db.Temperature;
import org.temperature.model.db.Thermometer;
import org.temperature.repository.TemperatureRepository;
import org.temperature.repository.ThermometerRepository;

@Component
public class TemperatureListener {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    ThermometerRepository thermometerRepository;
    private static final Logger log = LoggerFactory.getLogger(TemperatureListener.class);

    @KafkaListener(id = "1", topics = "#{environment.TEMPERATURE_TOPIC_NAME}")
    public void listen(TemperatureMeasurement measurement) {
        log.info("Received TemperatureMeasurement " + measurement);

        Thermometer thermometer = thermometerRepository.findByThermometerName(measurement.thermometerName());
        Temperature temperature = new Temperature();
        temperature.setTemperature(measurement.temperature());
        temperature.setTimestampMs(measurement.timestampMs());
        temperature.setThermometer(thermometer);
        temperatureRepository.save(temperature);
    }
}
