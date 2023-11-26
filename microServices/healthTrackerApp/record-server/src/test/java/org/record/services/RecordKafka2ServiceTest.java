package org.record.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;

import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.record.RecordRepository;
import org.record.client.dto.User;
import org.record.config.TestKafkaConfiguration;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import com.google.gson.Gson;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Import(TestKafkaConfiguration.class)
public class RecordKafka2ServiceTest {

    @Autowired
    private ConsumerFactory<String, String> consumerFactory2;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private Gson gson;

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
        consumer.subscribe(Collections.singletonList("user-first-creation"));
    }

    @AfterEach
    public void tearDown() {

        consumer.close();
        recordRepository.deleteAll();
    }

    @Test
    public void addNewRecordByUserId_SuccessConsumeMessageWithValidData_CreatesRecord() throws InterruptedException {

        String userToken = gson.toJson(userView);

        kafkaTemplate.send("USER_FIRST_CREATION", userToken);

        Thread.sleep(5000);

        assertFalse(recordRepository.findAll().isEmpty());
    }

    @Test
    public void addNewRecordByUserId_SuccessConsumeMessageWithInvalidData_EmptyRepository()
            throws InterruptedException {
        String message = "invalid";
        kafkaTemplate.send("USER_FIRST_CREATION", message);

        Thread.sleep(5000);

        assertTrue(recordRepository.findAll().isEmpty());
    }

}
