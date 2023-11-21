// package org.record;

// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.time.Duration;
// import java.util.Collections;

// import org.apache.kafka.clients.consumer.Consumer;
// import org.apache.kafka.clients.consumer.ConsumerRecords;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.record.config.TestKafkaConfiguration;
// import org.record.model.dtos.UserView;
// import org.record.model.entity.Record;
// import org.record.model.enums.Gender;
// import org.record.model.enums.WorkoutState;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.test.context.EmbeddedKafka;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// import com.google.gson.Gson;

// @SpringBootTest
// @ActiveProfiles("test")
// @AutoConfigureMockMvc
// @EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
// @Import(TestKafkaConfiguration.class)
// public class RecordControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private RecordRepository recordRepository;

//     private Gson gson = new Gson();
//     private UserView userView;

//     @Autowired
//     private ConsumerFactory<String, RecordCreation> consumerFactory;

//     private Consumer<String, RecordCreation> consumer;

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

//         consumer = consumerFactory.createConsumer("testGroup", "test");
//         consumer.subscribe(Collections.singletonList("record-creation"));
//     }

//     @Test
//     public void createNewRecord_ValidInputHeader_StatusOk() throws Exception {

//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isCreated());

//         Thread.sleep(5000);

//         ConsumerRecords<String, RecordCreation> records = consumer.poll(Duration.ofSeconds(5));

//         assertFalse(records.isEmpty());
//     }

//     @Test
//     public void createNewRecord_MissingOrNegativeAge_StatusBadRequest() throws Exception {

//         userView.setAge(null);
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());

//         userView.setAge(-1);
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void createNewRecord_MissingGender_StatusBadRequest() throws Exception {

//         userView.setGender("");
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void createNewRecord_MissingOrNegativeHeight_StatusBadRequest() throws Exception {

//         userView.setHeight("-1");
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());

//         userView.setHeight(null);
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void createNewRecord_NegativeOrMissingKilograms_StatusBadRequest() throws Exception {

//         userView.setKilograms("-1");
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());

//         userView.setKilograms("");
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void createNewRecord_MissingWorkoutState_StatusBadRequest() throws Exception {

//         userView.setWorkoutState(null);
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void createNewRecord_MissingOrInvalidEmail_StatusBadRequest() throws Exception {

//         userView.setEmail(null);
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());

//         userView.setEmail("test");
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView))).andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void createNewRecord_MissingOrInvalidUsername_StatusBadRequest() throws Exception {

//         userView.setUsername(null);
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView))).andExpect(MockMvcResultMatchers.status().isBadRequest());

//         userView.setUsername("");
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView))).andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void deleteById_ValidInputHeader_StatusNoContent() throws Exception {

//         Record record = new Record();
//         record.setId(1L);
//         record.setUserId(userView.getId());
//         record = recordRepository.save(record);

//         mockMvc.perform(MockMvcRequestBuilders.delete("/api/record/" + record.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isNoContent());

//         assertTrue(recordRepository.findById(record.getId()).isEmpty());
//     }

//     @Test 
//     public void deleteById_InvalidRecordId_StatusNotFound() throws Exception {
//         Long invalidRecordId = 2L;
//         mockMvc.perform(MockMvcRequestBuilders.delete("/api/record/" + invalidRecordId)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView)))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void createStorage_ValidInputHeader_StatusNoContent() throws Exception {

//         Record record = new Record();
//         record.setId(3L);
//         record.setUserId(userView.getId());
//         record = recordRepository.save(record);

//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record/" + record.getId() + "/storage")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView))
//                 .param("storageName", "test"))
//                 .andExpect(MockMvcResultMatchers.status().isNoContent());

//         assertFalse(recordRepository.findById(record.getId()).isEmpty());
//     }

//     @Test
//     public void createStorage_InvalidRecordId_StatusBadRequest() throws Exception {
//         Long invalidRecordId = 4L;
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/record/" + invalidRecordId +"/storage")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView))
//                 .param("storageName", "test"))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

//     @Test
//     public void deleteStorage_ValidInputHeader_StatusNoContent() throws Exception {

//         Record record = new Record();
//         record.setId(5L);
//         record.setUserId(userView.getId());
//         record = recordRepository.save(record);

//         mockMvc.perform(MockMvcRequestBuilders.delete("/api/record/" + record.getId() + "/storage")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView))
//                 .param("storageId", "1"))
//                 .andExpect(MockMvcResultMatchers.status().isNoContent());

//         assertFalse(recordRepository.findById(record.getId()).isEmpty());
//     }
//     @Test 
//     public void deleteStorage_InvalidInput_StatusBadRequest() throws Exception{
//         Long invalidRecordId = 6L;

//         mockMvc.perform(MockMvcRequestBuilders.delete("/api/record/" + invalidRecordId + "/storage")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header("X-ViewUser", gson.toJson(userView))
//                 .param("storageId", "1"))
//                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
//     }

// }
