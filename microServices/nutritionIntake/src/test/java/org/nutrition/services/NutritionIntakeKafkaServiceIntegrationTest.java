package org.nutrition.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.nutrition.NutritionIntakeRepository;
import org.nutrition.model.dtos.RecordCreation;
import org.nutrition.utils.NutrientIntakeCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class NutritionIntakeKafkaServiceIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.1.0")
            .withDatabaseName("nutritionIntake")
            .withExposedPorts(3307, 3306)
            .withUsername("root")
            .withPassword("12345");

    @Autowired
    private NutritionIntakeRepository repository;

    @Autowired
    private NutrientIntakeCreator nutrientIntakeCreator;

    @Test
    public void testKafkaListener() throws InterruptedException {
        RecordCreation recordCreation = new RecordCreation();
        KafkaTemplate<String , RecordCreation > kafkaTemplate = createKafkaTemplate();
        kafkaTemplate.send("record-creation", recordCreation);
    }

    private KafkaTemplate<String, RecordCreation> createKafkaTemplate() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    ProducerFactory<String, RecordCreation> producerFactory = new DefaultKafkaProducerFactory<>(props);
    return new KafkaTemplate<>(producerFactory);
}
}