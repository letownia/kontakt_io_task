package org.temperature;

import org.temperature.model.TemperatureMeasurement;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.kafka.support.serializer.JsonSerializer;

import static java.lang.System.currentTimeMillis;

public class ProducerMain {
    private static final Logger log = LoggerFactory.getLogger(ProducerMain.class);

    /**
     * Randomly modifies original by a small amount.
     * <p>
     * Current implementation is 10% to modify by 0 to 5
     *
     * @param original
     * @return
     */
    static Double fluctuateRandomly(Double original) {
        if (new Random().nextDouble() > 0.9) {
            return original + new Random().nextDouble() * 5;
        } else {
            return original;
        }
    }

    public static void main(String[] args) {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String temperatureTopicName = System.getenv("TEMPERATURE_TOPIC_NAME");
        Integer eventDelayMs = Integer.valueOf(System.getenv("EVENT_DELAY_MS"));
        Integer number_of_events = Integer.valueOf(System.getenv("NUMBER_OF_EVENTS"));

        Objects.requireNonNull(bootstrapServers, "Missing required param BOOTSTRAP_SERVERS");
        Objects.requireNonNull(temperatureTopicName, "Missing required param TEMPERATURE_TOPIC_NAME");
        Objects.requireNonNull(eventDelayMs, "Missing required param EVENT_DELAY_MS");


        log.info("Producer Starting, connecting to server {} and topic {} .", bootstrapServers, temperatureTopicName);
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        //TODO - decide on a more sensible value (1500 is for speed of testing)
        properties.put("max.block.ms", 1500);

        KafkaProducer<String, TemperatureMeasurement> producer = new KafkaProducer<>(properties);


        ThreadLocalRandom.current().doubles(number_of_events, 20, 25).map(ProducerMain::fluctuateRandomly)
                .mapToObj(x -> new ProducerRecord<String, TemperatureMeasurement>(temperatureTopicName,
                        new TemperatureMeasurement(currentTimeMillis(), x)))
                .peek(x -> {
                            if (eventDelayMs > 0) {
                                try {
                                    Thread.sleep(eventDelayMs);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            log.info("Producer sending Measurement : {}", x.value());

                        }
                ).forEach(x -> producer.send(x));

        producer.flush();
    }


    private static ProducerRecord<String, TemperatureMeasurement> generateProducerRecord(String topicName) {
        return new ProducerRecord<>(topicName, new TemperatureMeasurement(currentTimeMillis(), 21.0));
    }
}