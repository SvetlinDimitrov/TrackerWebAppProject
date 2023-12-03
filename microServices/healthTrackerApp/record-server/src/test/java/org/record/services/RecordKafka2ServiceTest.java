package org.record.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;

import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.record.RecordRepository;
import org.record.client.StorageClient;
import org.record.client.dto.User;
import org.record.config.TestKafkaConfiguration;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.record.utils.GsonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Import(TestKafkaConfiguration.class)
public class RecordKafka2ServiceTest {

    @Mock
    private StorageClient storageClient;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory2;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private GsonWrapper gson;

    private Consumer<String, String> consumer;

    private User userView;

    @BeforeEach
    public void setUp() {

        userView = new User();
        userView.setAge(20);
        userView.setEmail("test@abv.bg");
        userView.setGender(Gender.FEMALE);
        userView.setHeight(new BigDecimal("170"));
        userView.setKilograms(new BigDecimal("60"));
        userView.setUsername("test");
        userView.setWorkoutState(WorkoutState.MODERATELY_ACTIVE);

        consumer = consumerFactory2.createConsumer("testGroupString", "test");
        consumer.subscribe(Collections.singletonList("USER_FIRST_CREATION"));
        consumer.subscribe(Collections.singletonList("USER_DELETION"));
    }

    @AfterEach
    public void tearDown() {

        consumer.close();
        recordRepository.deleteAll();
    }

    @Test
    public void testAddNewRecordByUserId_SuccessConsumeMessageWithValidData_CreatesRecord() throws InterruptedException {

        userView.setId(1L);
        String userToken = gson.toJson(userView);

        kafkaTemplate.send("USER_FIRST_CREATION", userToken);

        Thread.sleep(5000);

        assertTrue(recordRepository.findAllByUserId(userView.getId()).size() != 0);
    }

    @Test
    public void addNewRecordByUserId_SuccessConsumeMessageWithInvalidData_EmptyRepository()
            throws InterruptedException {
        String message = "invalid";
        kafkaTemplate.send("USER_FIRST_CREATION", message);

        Thread.sleep(20000);

        assertTrue(recordRepository.findAll().isEmpty());
    }


}
