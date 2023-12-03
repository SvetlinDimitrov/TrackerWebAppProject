package org.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.storage.client.FoodClient;
import org.storage.client.FoodUpdate;
import org.storage.client.User;
import org.storage.model.entity.Food;
import org.storage.model.entity.Storage;

import com.google.gson.Gson;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StorageControllerIntegrationTest {

    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private StorageService storageService;

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    FoodClient foodClient = Mockito.mock(FoodClient.class);

    @BeforeEach
    public void setUp() {

        Mockito.when(foodClient.getFoodByName("Apple")).thenReturn(getApple());
        Mockito.when(foodClient.getFoodByName("Invalid")).thenThrow(new RuntimeException("Invalid food name"));

        ReflectionTestUtils.setField(storageService, "foodClient", foodClient);
    }

    @AfterEach
    public void tearDown() {
        storageRepository.deleteAll();
    }

    @Test
    public void testGetAllStorages_ValidINput_statusOK() throws Exception {

        Long validRecordId = 1L;
        User user = getUser(1L);

        Storage storage = createStorage(validRecordId, user.getId());
        storageRepository.save(storage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/all?recordId=" + validRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testGetAllStorages_InValidINput_badRequest() throws Exception {

        Long invalidRecordId = 1000L;
        User user = getUser(2L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/all?recordId=" + invalidRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testGetStorage_ValidINput_statusOK() throws Exception {

        Long validRecordId = 2L;
        User user = getUser(3L);

        Storage storage = createStorage(validRecordId, user.getId());
        storageRepository.save(storage);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/storage?recordId=" + validRecordId + "&storageId="
                                + storage.getId())
                        .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testGetStorage_InValidINput_badRequest() throws Exception {

        Long invalidRecordId = 1000L;
        Long invalidStorageId = 1000L;
        User user = getUser(4L);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/storage?recordId=" + invalidRecordId + "&storageId=" + invalidStorageId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testCreateStorage_ValidInputWitNoName_statusNoContent() throws Exception {

        Long validRecordId = 3L;
        User user = getUser(5L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage?recordId=" + validRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).size() == 1);
    }

    @Test
    public void testCreateStorage_ValidInputWitName_statusNoContent() throws Exception {

        Long validRecordId = 4L;
        String storageName = "test";
        User user = getUser(6L);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/storage?recordId=" + validRecordId + "&storageName="
                                + storageName)
                        .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findByNameAndRecordIdAndUserId(storageName, validRecordId, user.getId())
                .isPresent());
    }

    @Test
    public void testCreateStorageFirstCreation_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 5L;
        User user = getUser(7L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/firstCreation?recordId=" + validRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).size() == 4);
    }

    @Test
    public void testDeleteStorage_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 6L;
        User user = getUser(8L);
        Storage storage = createStorage(validRecordId, user.getId());
        storageRepository.save(storage);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/storage/delete/" + storage.getId() + "/record?recordId=" + validRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).size() == 0);
    }

    @Test
    public void testDeleteStorage_InValidInput_badRequest() throws Exception {

        Long invalidRecordId = 1000L;
        Long invalidStorageId = 1000L;
        User user = getUser(9L);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/storage/delete/" + invalidStorageId + "/record?recordId="
                        + invalidRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testDeleteAllStoragesByRecordId_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 7L;
        User user = getUser(10L);
        Storage storage = createStorage(validRecordId, user.getId());
        Storage storage1 = createStorage(validRecordId, user.getId());
        Storage storage2 = createStorage(validRecordId, user.getId());
        storageRepository.saveAll(List.of(storage, storage1, storage2));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/storage/delete/all?recordId=" + validRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).size() == 0);
    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 8L;
        User user = getUser(11L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getFoods()
                .size() == 1);

    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_ValidInputWithCustomFood_statusNoContent() throws Exception {

        Long validRecordId = 9L;
        User user = getUser(12L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food customFood = getCustomFood();
        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(customFood.getSize());
        foodUpdate.setFoodName(customFood.getName());

        storageRepository.save(storage);

        Mockito.when(foodClient.getCustomFoodByNameAndSize(
                foodUpdate.getFoodName(),
                foodUpdate.getAmount().doubleValue(),
                gson.toJson(user))).thenReturn(customFood);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 1);
    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_ValidInputWithCustomFoodAndCustomFoodAlreadyExists_statusNoContent()
            throws Exception {

        Long validRecordId = 10L;
        User user = getUser(13L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food customFood = getCustomFood();
        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(customFood.getSize());
        foodUpdate.setFoodName(customFood.getName());

        storageRepository.save(storage);

        Mockito.when(foodClient.getCustomFoodByNameAndSize(
                foodUpdate.getFoodName(),
                foodUpdate.getAmount().doubleValue(),
                gson.toJson(user))).thenReturn(customFood);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 1);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 1);
        assertTrue(storage.getConsumedCalories()
                .compareTo(getCustomFood().getCalories().multiply(new BigDecimal(2))) == 0);
    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_ValidInputCustomFoodAndNormalFoodWithSameNames_statusNoContent()
            throws Exception {
        Long validRecordId = 11L;
        User user = getUser(14L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food customFood = getCustomFood();
        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(customFood.getSize());
        foodUpdate.setFoodName(customFood.getName());

        storageRepository.save(storage);

        Mockito.when(foodClient.getCustomFoodByNameAndSize(
                foodUpdate.getFoodName(),
                foodUpdate.getAmount().doubleValue(),
                gson.toJson(user))).thenReturn(customFood);

        String foodUpdateJson = gson.toJson(foodUpdate);

        Mockito.when(foodClient.getFoodByName(foodUpdate.getFoodName())).thenReturn(getApple());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 1);

        foodUpdate.setAmount(getApple().getSize());
        foodUpdate.setFoodName(getApple().getName());
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "false")
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getFoods()
                .size() == 1);
        assertTrue(storage.getConsumedCalories()
                .compareTo(getCustomFood().getCalories().add(getApple().getCalories())) == 0);
    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_InvalidFruitName_badRequest() throws Exception {

        Long validRecordId = 9L;
        User user = getUser(12L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Invalid");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_stackingTheSameFruit_oneFruitWithIncreasedSize() throws Exception {

        Long validRecordId = 10L;
        User user = getUser(13L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        assertTrue(storageAfter.getFoods().size() == 1);
        assertTrue(storageAfter.getFoods().get("Apple").getSize().compareTo(new BigDecimal(200)) == 0);
        assertTrue(storageAfter.getConsumedCalories()
                .compareTo(getApple().getCalories().add(getApple().getCalories())) == 0);

    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_invalidSize_badRequest() throws Exception {

        Long validRecordId = 11L;
        User user = getUser(14L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(-100));
        foodUpdate.setFoodName("Apple");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_invalidRecordId_badRequest() throws Exception {

        Long invalidRecordId = 1000L;
        User user = getUser(14L);
        Storage storage = createStorage(invalidRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + invalidRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 12L;
        User user = getUser(15L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodUpdate.setAmount(new BigDecimal(200));
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        assertTrue(storageAfter.getFoods().size() == 1);
        assertTrue(storageAfter.getFoods().get("Apple").getSize().compareTo(new BigDecimal(200)) == 0);
        assertTrue(storageAfter.getConsumedCalories()
                .compareTo(getApple().getCalories().multiply(new BigDecimal(2))) == 0);

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_ValidInputCustomFood_statusNoContent() throws Exception {

        Long validRecordId = 13L;
        User user = getUser(16L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food customFood = getCustomFood();
        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(customFood.getSize());
        foodUpdate.setFoodName(customFood.getName());

        storageRepository.save(storage);

        Mockito.when(foodClient.getCustomFoodByNameAndSize(
                foodUpdate.getFoodName(),
                foodUpdate.getAmount().doubleValue(),
                gson.toJson(user))).thenReturn(customFood);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodUpdate.setAmount(new BigDecimal(200));
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        assertTrue(storageAfter.getCustomFoods().size() == 1);
        assertTrue(storageAfter.getCustomFoods().get(getCustomFood().getName()).getSize()
                .compareTo(new BigDecimal(200)) == 0);
        assertTrue(storageAfter.getConsumedCalories()
                .compareTo(getCustomFood().getCalories().multiply(new BigDecimal(2))) == 0);

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_InvalidInput_badRequest() throws Exception {

        Long validRecordId = 13L;
        User user = getUser(16L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodUpdate.setAmount(new BigDecimal(-200));
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_InvalidRecordId_badRequest() throws Exception {

        Long invalidRecordId = 1000L;
        User user = getUser(17L);
        Storage storage = createStorage(invalidRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + invalidRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_invalidFruitName_badRequest() throws Exception {

        Long validRecordId = 14L;
        User user = getUser(18L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodUpdate.setFoodName("Invalid");
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_customFoodNormalFoodWithTheSameName_TwoFoodsNoContent()
            throws Exception {
        Long validRecordId = 15L;
        User user = getUser(19L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food customFood = getCustomFood();
        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(customFood.getSize());
        foodUpdate.setFoodName(customFood.getName());

        storageRepository.save(storage);

        Mockito.when(foodClient.getCustomFoodByNameAndSize(
                foodUpdate.getFoodName(),
                foodUpdate.getAmount().doubleValue(),
                gson.toJson(user))).thenReturn(customFood);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodUpdate.setAmount(getApple().getSize());
        foodUpdate.setFoodName(getApple().getName());
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodUpdate.setAmount(getApple().getSize().divide(new BigDecimal(2)));
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId + "&isCustom=false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        assertTrue(storageAfter.getCustomFoods().size() == 1);
        assertTrue(storageAfter.getFoods().size() == 1);
        assertTrue(storageAfter.getCustomFoods().get(getCustomFood().getName()).getSize()
                .compareTo(new BigDecimal(50)) == 0);
        assertTrue(storageAfter.getFoods().get(getApple().getName()).getSize().compareTo(new BigDecimal(50)) == 0);
        assertTrue(storageAfter.getConsumedCalories()
                .compareTo(getCustomFood().getCalories().divide(new BigDecimal(2))
                        .add(getApple().getCalories().divide(new BigDecimal(2)))) == 0);
    }

    @Test
    @Transactional
    public void testDeleteFoodFromStorage_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 15L;
        User user = getUser(19L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/removeFood?recordId=" + validRecordId
                        + "&foodName=Apple")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getFoods()
                .size() == 0);
        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0)
                .getConsumedCalories()
                .compareTo(new BigDecimal(0)) == 0);
    }
    @Test
    @Transactional
    public void testDeleteFoodFromStorage_CustomFoodInput_statusNoContent() throws Exception {

        Long validRecordId = 15L;
        User user = getUser(19L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food customFood = getCustomFood();
        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(customFood.getSize());
        foodUpdate.setFoodName(customFood.getName());

        storageRepository.save(storage);

        Mockito.when(foodClient.getCustomFoodByNameAndSize(
                foodUpdate.getFoodName(),
                foodUpdate.getAmount().doubleValue(),
                gson.toJson(user))).thenReturn(customFood);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/removeFood?recordId=" + validRecordId
                        + "&foodName=Apple" + "&isCustom=true")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 0);
        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0)
                .getConsumedCalories()
                .compareTo(new BigDecimal(0)) == 0);
    }

    @Test
    @Transactional
    public void testDeleteFoodFromStorage_AddedCustomAndNormalFoodDeleteCustomFood_NormalStillAcresNoContent()
        throws Exception {

        Long validRecordId = 15L;
        User user = getUser(19L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food customFood = getCustomFood();
        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(customFood.getSize());
        foodUpdate.setFoodName(customFood.getName());

        storageRepository.save(storage);

        Mockito.when(foodClient.getCustomFoodByNameAndSize(
                foodUpdate.getFoodName(),
                foodUpdate.getAmount().doubleValue(),
                gson.toJson(user))).thenReturn(customFood);

        String foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodUpdate.setAmount(getApple().getSize());
        foodUpdate.setFoodName(getApple().getName());
        foodUpdateJson = gson.toJson(foodUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/removeFood?recordId=" + validRecordId
                        + "&foodName=Apple" + "&isCustom=true")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 0);
        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getFoods()
                .size() == 1);
        
    }
    @Test
    @Transactional
    public void testDeleteFoodFromStorage_InvalidInput_badRequest() throws Exception {

        Long invalidRecordId = 16L;
        User user = getUser(20L);
        Storage storage = createStorage(invalidRecordId, user.getId());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/removeFood?recordId=" + invalidRecordId
                        + "&foodName=Apple")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testDeleteFoodFromStorage_InvalidFoodName_badRequest() throws Exception {

        Long validRecordId = 17L;
        User user = getUser(21L);
        Storage storage = createStorage(validRecordId, user.getId());

        storageRepository.save(storage);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/removeFood?recordId=" + validRecordId
                        + "&foodName=Invalid")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    private Food getApple() {
        return Food.builder()
                .name("Apple")
                .calories(new BigDecimal("52"))
                .size(new BigDecimal("100"))
                .A(new BigDecimal("0"))
                .D(new BigDecimal("0"))
                .E(new BigDecimal("0.18"))
                .K(new BigDecimal("0.5"))
                .C(new BigDecimal("0.5"))
                .B1(new BigDecimal("0.017"))
                .B2(new BigDecimal("0.026"))
                .B3(new BigDecimal("0.091"))
                .B5(new BigDecimal("0.043"))
                .B6(new BigDecimal("0.041"))
                .B7(new BigDecimal("0.009"))
                .B9(new BigDecimal("0.003"))
                .B12(new BigDecimal("0"))
                .Calcium(new BigDecimal("6"))
                .Phosphorus(new BigDecimal("11"))
                .Magnesium(new BigDecimal("5"))
                .Sodium(new BigDecimal("1"))
                .Potassium(new BigDecimal("107"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("0.12"))
                .Zinc(new BigDecimal("0.05"))
                .Copper(new BigDecimal("0.05"))
                .Manganese(new BigDecimal("0.035"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("0"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("14"))
                .Protein(new BigDecimal("0.3"))
                .Fat(new BigDecimal("0.2"))
                .Fiber(new BigDecimal("2.4"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("0.02"))
                .Sugar(new BigDecimal("10"))
                .PolyunsaturatedFat(new BigDecimal("0.03"))
                .MonounsaturatedFat(new BigDecimal("0.01"))
                .build();
    }

    private Food getCustomFood() {
        return Food.builder()
                .name("Apple")
                .calories(new BigDecimal("250"))
                .size(new BigDecimal("100"))
                .A(new BigDecimal("0"))
                .D(new BigDecimal("0"))
                .E(new BigDecimal("0.18"))
                .K(new BigDecimal("0.5"))
                .C(new BigDecimal("0.5"))
                .B1(new BigDecimal("0.017"))
                .B2(new BigDecimal("0.026"))
                .B3(new BigDecimal("0.091"))
                .B5(new BigDecimal("0.043"))
                .B6(new BigDecimal("0.041"))
                .B7(new BigDecimal("0.009"))
                .B9(new BigDecimal("0.003"))
                .B12(new BigDecimal("0"))
                .Calcium(new BigDecimal("6"))
                .Phosphorus(new BigDecimal("11"))
                .Magnesium(new BigDecimal("5"))
                .Sodium(new BigDecimal("1"))
                .Potassium(new BigDecimal("107"))
                .Chloride(new BigDecimal("0"))
                .Iron(new BigDecimal("0.12"))
                .Zinc(new BigDecimal("0.05"))
                .Copper(new BigDecimal("0.05"))
                .Manganese(new BigDecimal("0.035"))
                .Iodine(new BigDecimal("0"))
                .Selenium(new BigDecimal("0"))
                .Fluoride(new BigDecimal("0"))
                .Chromium(new BigDecimal("0"))
                .Molybdenum(new BigDecimal("0"))
                .Carbohydrates(new BigDecimal("14"))
                .Protein(new BigDecimal("0.3"))
                .Fat(new BigDecimal("0.2"))
                .Fiber(new BigDecimal("2.4"))
                .TransFat(new BigDecimal("0"))
                .SaturatedFat(new BigDecimal("0.02"))
                .Sugar(new BigDecimal("10"))
                .PolyunsaturatedFat(new BigDecimal("0.03"))
                .MonounsaturatedFat(new BigDecimal("0.01"))
                .build();
    }

    private User getUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }

    private Storage createStorage(Long recordId, Long userId) {
        Storage entity = new Storage();
        entity.setName("test");
        entity.setUserId(userId);
        entity.setFoods(new HashMap<>());
        entity.setCustomFoods(new HashMap<>());
        entity.setRecordId(recordId);
        return entity;
    }

}
