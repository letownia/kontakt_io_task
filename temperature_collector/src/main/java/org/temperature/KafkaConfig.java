package org.temperature;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.temperature.model.TemperatureMeasurement;
import org.temperature.model.db.Room;


import java.awt.print.Book;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class KafkaConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);


    @Autowired
    private Environment environment;


    @Bean
    public ConsumerFactory<Integer, TemperatureMeasurement> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(defaultKafkaConfig(),
                new StringDeserializer(),
                new JsonDeserializer(TemperatureMeasurement.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, TemperatureMeasurement>
    kafkaListenerContainerFactory(ConsumerFactory<Integer, TemperatureMeasurement> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Integer, TemperatureMeasurement> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);
        return factory;
    }

    private Map<String, Object> defaultKafkaConfig() {
        log.info(" environment.getProperty(\"BOOTSTRAP_SERVERS\")=" +  environment.getProperty("BOOTSTRAP_SERVERS"));
        log.info("Initializing props "+ System.getenv("BOOTSTRAP_SERVERS"));
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("BOOTSTRAP_SERVERS"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "temperature_group_name");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

}