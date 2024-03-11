package org.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TemperatureListener {

    private static final Logger log = LoggerFactory.getLogger(TemperatureListener.class);

    @KafkaListener(id = "1", topics = "#{environment.TEMPERATURE_TOPIC_NAME}")
    public void listen(String in) {
        log.info("Received message " + in);
    }
}
