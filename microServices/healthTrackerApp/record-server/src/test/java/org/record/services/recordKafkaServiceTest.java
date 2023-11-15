// package org.record.services;

// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.Collections;

// import org.apache.kafka.clients.consumer.Consumer;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.record.RecordRepository;
// import org.record.client.dto.RecordCreation;
// import org.record.config.TestKafkaConfiguration;
// import org.record.exceptions.RecordNotFoundException;
// import org.record.model.dtos.UserView;
// import org.record.model.entity.Record;
// import org.record.model.enums.Gender;
// import org.record.model.enums.WorkoutState;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.test.context.EmbeddedKafka;
// import org.springframework.test.context.ActiveProfiles;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

// @SpringBootTest
// @ActiveProfiles("test")
// @EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
// @Import(TestKafkaConfiguration.class)
// public class recordKafkaServiceTest {

//     @Autowired
//     private ConsumerFactory<String, RecordCreation> consumerFactory;
//     @Autowired
//     private ConsumerFactory<String, String> consumerFactory2;
//     @Autowired
//     private KafkaTemplate<String, String> kafkaTemplate;
//     @Autowired
//     private RecordRepository recordRepository;
//     @Autowired
//     private RecordKafkaService recordKafkaService;

//     private Consumer<String, RecordCreation> consumer;
//     private Consumer<String, String> consumer2;
//     private UserView userView;
//     private ObjectMapper objectMapper;

//     @BeforeEach
//     public void setUp() {

//         userView = new UserView();
//         userView.setAge(20);
//         userView.setEmail("test@abv.bg");
//         userView.setGender(Gender.FEMALE.name());
//         userView.setHeight("170");
//         userView.setKilograms("60");
//         userView.setUsername("test");
//         userView.setWorkoutState(WorkoutState.MODERATELY_ACTIVE.name());

//         objectMapper = new ObjectMapper();

//         consumer = consumerFactory.createConsumer("testGroup", "test");
//         consumer.subscribe(Collections.singletonList("record-creation"));
//         consumer2 = consumerFactory2.createConsumer("testGroupString", "test");
//         consumer2.subscribe(Collections.singletonList("user-first-creation"));
//     }

//     @AfterEach
//     public void tearDown() {
//         consumer.close();
//         consumer2.close();
//         recordRepository.deleteAll();
//     }

    
//     @Test
//     public void addNewRecordByUserId_SuccessConsumeMessageWithValidData_CreatesRecord()
//             throws InterruptedException, JsonProcessingException {
//         ObjectMapper objectMapper = new ObjectMapper();
//         String userToken = objectMapper.writeValueAsString(userView);

//         kafkaTemplate.send("user-first-creation", userToken);

//         Thread.sleep(5000);

//         assertFalse(recordRepository.findAll().isEmpty());
//     }

   
//     @Test
//     public void addNewRecordByUserId_SuccessConsumeMessageWithInvalidData_EmptyRepository()
//             throws InterruptedException, JsonProcessingException {
//         String message = "invalid";
//         kafkaTemplate.send("user-first-creation", message);

//         Thread.sleep(5000);

//         assertTrue(recordRepository.findAll().isEmpty());
//     }

//     @Test
//     public void deleteById_ValidData_Successful() throws JsonProcessingException, RecordNotFoundException {
//         Long userId = 11L;

//         userView.setId(userId);
//         Record record = getRecord(userId);

//         String userToken = objectMapper.writeValueAsString(userView);

//         recordRepository.saveAndFlush(record);

//         recordKafkaService.deleteById(record.getId(), userToken);

//         assertTrue(recordRepository.findById(record.getId()).isEmpty());
//     }

//     @Test
//     public void deleteById_InvalidData_throwsException() throws JsonProcessingException, RecordNotFoundException {
//         Long userId = 11L;
//         Long invalidUserId = 12L;

//         userView.setId(userId);
//         Record record = getRecord(invalidUserId);

//         String userToken = objectMapper.writeValueAsString(userView);

//         recordRepository.saveAndFlush(record);

//         assertThrows(RecordNotFoundException.class,
//                 () -> recordKafkaService.deleteById(record.getId(), userToken));
//     }

//     private Record getRecord(Long userId) {
//         Record record = new Record();
//         record.setUserId(userId);
//         return record;
//     }

// }
// not working on maven tests