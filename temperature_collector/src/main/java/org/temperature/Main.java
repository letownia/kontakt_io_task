package org.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String temperatureTopicName = System.getenv("TEMPERATURE_TOPIC_NAME");
        log.info("Starting TemperatureCollector");

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
    }

}