package letownia;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Properties properties = new Properties();
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String temperatureTopicName = System.getenv("TEMPERATURE_TOPIC_NAME");
        if(bootstrapServers == null) {
            bootstrapServers = "0.0.0.0:9093";
        }

        log.info("Producer Starting and connecting to bootstrap server: " + bootstrapServers);

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //TODO - decide on a more sensible value (1500 is for speed of testing)
        properties.put("max.block.ms", 1500);

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(temperatureTopicName, "figure out this format");

        // send data - asynchronous
        Future<RecordMetadata> future = producer.send(producerRecord);

        try {
            log.info("Future : " + future.get());
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