package letownia;

import letownia.model.TemperatureMeasurement;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String temperatureTopicName = System.getenv("TEMPERATURE_TOPIC_NAME");
        Objects.requireNonNull(bootstrapServers);
        Objects.requireNonNull(temperatureTopicName);


        log.info("Producer Starting, connecting to server {} and topic {} .", bootstrapServers, temperatureTopicName);
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        //TODO - decide on a more sensible value (1500 is for speed of testing)
        properties.put("max.block.ms", 1500);

        KafkaProducer<String, TemperatureMeasurement> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, TemperatureMeasurement> producerRecord =
                new ProducerRecord<>(temperatureTopicName, new TemperatureMeasurement(1.0, 21.0));

        // send data - asynchronous
        Future<RecordMetadata> future = producer.send(producerRecord);

        try {
            log.info("Future : {}", future.get());
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        } catch (ExecutionException e) {
            log.error("ExecutionException", e);

        }

        // flush data - synchronous
        producer.flush();
        // flush and close producer
        producer.close();
    }
}