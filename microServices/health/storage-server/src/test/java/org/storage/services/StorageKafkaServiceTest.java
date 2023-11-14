package org.storage.services;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.storage.StorageRepository;
import org.storage.model.dto.RecordCreation;
import org.storage.model.dto.StorageCreation;
import org.storage.model.dto.StorageDeletion;
import org.storage.model.entity.Storage;
import org.storage.model.enums.Gender;
import org.storage.model.enums.WorkoutState;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class StorageKafkaServiceTest {

    @Autowired
    private StorageRepository storageRepository;

    @Test
    public void recordFirstCreation_ValidMessage_StorageCreated() throws InterruptedException {

        RecordCreation recordCreation = new RecordCreation();
        recordCreation.setRecordId(20L);
        recordCreation.setWorkoutState(WorkoutState.LIGHTLY_ACTIVE);
        recordCreation.setGender(Gender.FEMALE);
        recordCreation.setCaloriesPerDay(new BigDecimal(2000.0));

        recordCreationKafkaTemplate().send("record-creation", recordCreation);

        Thread.sleep(5000);
        assertTrue(storageRepository.findAllByRecordId(20L).size() == 4);
    }

    @Test
    public void recordDeletion_ValidMessage_DeleteAllStoragesWithGivenRecordId() throws InterruptedException {

        storageRepository.saveAllAndFlush(
                List.of(new Storage("First Meal", 21L),
                        new Storage("Second Meal", 21L),
                        new Storage("Third Meal", 21L),
                        new Storage("Snacks", 21L)));

        recordDeletionKafkaTemplate().send("record-deletion", 21L);

        Thread.sleep(5000);
        assertTrue(storageRepository.findAllByRecordId(21L).size() == 0);
    }

    @Test
    public void createStorage_ValidMessage_StorageCreated() throws InterruptedException {

        StorageCreation storageCreation = new StorageCreation();
        storageCreation.setRecordId(22L);
        storageCreation.setStorageName("test");

        storageCreationKafkaTemplate().send("storage-creation", storageCreation);

        Thread.sleep(5000);
        assertTrue(storageRepository.findAllByRecordId(22L).size() == 1);
    }

    @Test
    public void deleteStorage_ValidMessage_DeleteStorageWithGIvenIdAndRecordId() throws InterruptedException {
        Storage storage = new Storage();
        storage.setRecordId(23L);
        storage.setName("test");
        storage.setId(23L);

        storageRepository.saveAndFlush(storage);

        StorageDeletion storageDeletion = new StorageDeletion();
        storageDeletion.setRecordId(23L);
        storageDeletion.setStorageId(23L);

        storageDeletionKafkaTemplate().send("storage-deletion", storageDeletion);
        Thread.sleep(5000);
        assertTrue(storageRepository.findById(23L).isEmpty());
    }

    private KafkaTemplate<String, RecordCreation> recordCreationKafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, RecordCreation> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }

    private KafkaTemplate<String, Long> recordDeletionKafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

        ProducerFactory<String, Long> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }

    private KafkaTemplate<String, StorageCreation> storageCreationKafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, StorageCreation> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }

    private KafkaTemplate<String, StorageDeletion> storageDeletionKafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, StorageDeletion> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }
}
