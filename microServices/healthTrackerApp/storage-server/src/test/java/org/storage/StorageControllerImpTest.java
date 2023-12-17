package org.storage;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.storage.client.User;
import org.storage.model.dto.FoodInsertDto;
import org.storage.model.entity.Food;
import org.storage.model.entity.Storage;

import com.google.gson.Gson;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StorageControllerImpTest {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

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
    public void testGetAllStorages_InValidINput_emptyList() throws Exception {

        Long invalidRecordId = 1000L;
        User user = getUser(2L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/all?recordId=" + invalidRecordId)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

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

        storageRepository.save(storage);
        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
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

        Food customFood = getApple();
        customFood.setId(validRecordId);

        FoodInsertDto foodInsertDto = getFoodInsertDto(customFood.getName());
        foodInsertDto.setSize(customFood.getSize());
        foodInsertDto.setId(validRecordId);
        foodInsertDto.setCalories(customFood.getCalories());

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodInsertDtoJson)
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

        Food customFood = getApple();
        customFood.setId(validRecordId);

        storageRepository.save(storage);

        FoodInsertDto foodInsertDto = getFoodInsertDto(customFood.getName());
        foodInsertDto.setCalories(customFood.getCalories());
        foodInsertDto.setId(validRecordId);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 1);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 1);
        assertTrue(storage.getConsumedCalories()
                .compareTo(customFood.getCalories().multiply(new BigDecimal(2))) == 0);
    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_ValidInputCustomFoodAndNormalFoodWithSameNames_statusNoContent()
            throws Exception {
        Long validRecordId = 11L;
        User user = getUser(14L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        foodInsertDto.setSize(new BigDecimal(100));
        foodInsertDto.setId(validRecordId);

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "true")
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setId(null);
        foodUpdateJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("isCustom", "false")
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getFoods()
                .size() == 1);
        assertTrue(storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0).getCustomFoods()
                .size() == 1);
    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_InvalidFruitName_badRequest() throws Exception {

        Long validRecordId = 9L;
        User user = getUser(12L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto(null);

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        foodInsertDtoJson = foodInsertDtoJson.replace("null", "");

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_stackingTheSameFruit_oneFruitWithIncreasedSize() throws Exception {

        Long validRecordId = 10L;
        User user = getUser(13L);
        Storage storage = createStorage(validRecordId, user.getId());

        Food apple = getApple();
        FoodInsertDto foodInsertDto = getFoodInsertDto(apple.getName());
        mapFood(foodInsertDto, apple);

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        Food result = storageAfter.getFoods().get("Apple");
        assertTrue(storageAfter.getFoods().size() == 1);
        assertTrue(result.getSize().compareTo(apple.getSize().multiply(new BigDecimal(2))) == 0);
        assertTrue(storageAfter.getConsumedCalories()
                .compareTo(apple.getCalories().multiply(new BigDecimal(2))) == 0);

        assertTrue(apple.getA().multiply(new BigDecimal(2)).compareTo(result.getA()) == 0);
        assertTrue(apple.getB1().multiply(new BigDecimal(2)).compareTo(result.getB1()) == 0);
        assertTrue(apple.getC().multiply(new BigDecimal(2)).compareTo(result.getC()) == 0);
        assertTrue(apple.getD().multiply(new BigDecimal(2)).compareTo(result.getD()) == 0);
        assertTrue(apple.getE().multiply(new BigDecimal(2)).compareTo(result.getE()) == 0);
        assertTrue(apple.getK().multiply(new BigDecimal(2)).compareTo(result.getK()) == 0);
        assertTrue(apple.getB2().multiply(new BigDecimal(2)).compareTo(result.getB2()) == 0);
        assertTrue(apple.getB3().multiply(new BigDecimal(2)).compareTo(result.getB3()) == 0);
        assertTrue(apple.getB6().multiply(new BigDecimal(2)).compareTo(result.getB6()) == 0);
        assertTrue(apple.getB5().multiply(new BigDecimal(2)).compareTo(result.getB5()) == 0);
        assertTrue(apple.getB9().multiply(new BigDecimal(2)).compareTo(result.getB9()) == 0);
        assertTrue(apple.getB12().multiply(new BigDecimal(2)).compareTo(result.getB12()) == 0);

        assertTrue(apple.getCalcium().multiply(new BigDecimal(2)).compareTo(result.getCalcium()) == 0);
        assertTrue(apple.getIron().multiply(new BigDecimal(2)).compareTo(result.getIron()) == 0);
        assertTrue(apple.getMagnesium().multiply(new BigDecimal(2)).compareTo(result.getMagnesium()) == 0);
        assertTrue(apple.getManganese().multiply(new BigDecimal(2)).compareTo(result.getManganese()) == 0);
        assertTrue(apple.getPhosphorus().multiply(new BigDecimal(2)).compareTo(result.getPhosphorus()) == 0);
        assertTrue(apple.getPotassium().multiply(new BigDecimal(2)).compareTo(result.getPotassium()) == 0);
        assertTrue(apple.getSodium().multiply(new BigDecimal(2)).compareTo(result.getSodium()) == 0);
        assertTrue(apple.getZinc().multiply(new BigDecimal(2)).compareTo(result.getZinc()) == 0);
        assertTrue(apple.getCopper().multiply(new BigDecimal(2)).compareTo(result.getCopper()) == 0);
        assertTrue(apple.getSelenium().multiply(new BigDecimal(2)).compareTo(result.getSelenium()) == 0);
        assertTrue(apple.getMolybdenum().multiply(new BigDecimal(2)).compareTo(result.getMolybdenum()) == 0);
        assertTrue(apple.getIodine().multiply(new BigDecimal(2)).compareTo(result.getIodine()) == 0);
        assertTrue(apple.getFluoride().multiply(new BigDecimal(2)).compareTo(result.getFluoride()) == 0);
        assertTrue(apple.getChromium().multiply(new BigDecimal(2)).compareTo(result.getChromium()) == 0);

        assertTrue(apple.getFat().multiply(new BigDecimal(2)).compareTo(result.getFat()) == 0);
        assertTrue(apple.getCarbohydrates().multiply(new BigDecimal(2)).compareTo(result.getCarbohydrates()) == 0);
        assertTrue(apple.getFiber().multiply(new BigDecimal(2)).compareTo(result.getFiber()) == 0);
        assertTrue(apple.getProtein().multiply(new BigDecimal(2)).compareTo(result.getProtein()) == 0);
        assertTrue(apple.getCalories().multiply(new BigDecimal(2)).compareTo(result.getCalories()) == 0);
        assertTrue(apple.getSugar().multiply(new BigDecimal(2)).compareTo(result.getSugar()) == 0);
        assertTrue(apple.getPolyunsaturatedFat().multiply(new BigDecimal(2))
                .compareTo(result.getPolyunsaturatedFat()) == 0);
        assertTrue(apple.getSaturatedFat().multiply(new BigDecimal(2)).compareTo(result.getSaturatedFat()) == 0);
        assertTrue(apple.getMonounsaturatedFat().multiply(new BigDecimal(2))
                .compareTo(result.getMonounsaturatedFat()) == 0);
        assertTrue(apple.getTransFat().multiply(new BigDecimal(2)).compareTo(result.getTransFat()) == 0);

    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_invalidSize_SetSizeToZeroNoContent() throws Exception {

        Long validRecordId = 11L;
        User user = getUser(14L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        foodInsertDto.setSize(new BigDecimal(-100));

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertTrue(storage.getFoods().get("Apple").getSize().compareTo(new BigDecimal(0)) == 0);
    }

    @Test
    @Transactional
    public void testAddFoodFromStorage_invalidRecordId_badRequest() throws Exception {

        Long invalidRecordId = 1000L;
        User user = getUser(14L);
        Storage storage = createStorage(invalidRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + invalidRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 12L;
        User user = getUser(15L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        foodInsertDto.setSize(new BigDecimal(100));

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setSize(new BigDecimal(200));
        foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        assertTrue(storageAfter.getFoods().size() == 1);
        assertTrue(storageAfter.getFoods().get("Apple").getSize().compareTo(new BigDecimal(200)) == 0);
        assertTrue(storageAfter.getConsumedCalories()
                .compareTo(foodInsertDto.getCalories().multiply(new BigDecimal(2))) == 0);

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_ValidInputCustomFood_statusNoContent() throws Exception {

        Long validRecordId = 13L;
        User user = getUser(16L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        foodInsertDto.setSize(new BigDecimal(100));
        foodInsertDto.setId(validRecordId);

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setSize(new BigDecimal(200));
        foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        assertTrue(storageAfter.getCustomFoods().size() == 1);
        assertTrue(storageAfter.getCustomFoods().get("Apple").getSize().compareTo(new BigDecimal(200)) == 0);
        assertTrue(storageAfter.getConsumedCalories()
                .compareTo(foodInsertDto.getCalories().multiply(new BigDecimal(2))) == 0);

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_InvalidRecordId_badRequest() throws Exception {

        Long invalidRecordId = 1000L;
        User user = getUser(17L);
        Storage storage = createStorage(invalidRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + invalidRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_invalidFruitName_badRequest() throws Exception {

        Long validRecordId = 14L;
        User user = getUser(18L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        foodInsertDto.setSize(new BigDecimal(100));

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setName("Invalid");
        foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testChangeFoodAmountFromStorage_invalidCustomFruitName_badRequest() throws Exception {

        Long validRecordId = 14L;
        User user = getUser(18L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        foodInsertDto.setSize(new BigDecimal(100));
        foodInsertDto.setId(validRecordId);

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))

                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setName("Invalid");
        foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
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

        Food customFood = getApple();

        FoodInsertDto foodInsertDto = getFoodInsertDto(customFood.getName());
        foodInsertDto.setSize(customFood.getSize());
        foodInsertDto.setId(validRecordId);

        storageRepository.save(storage);

        String foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setId(null);
        foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setSize(getApple().getSize().divide(new BigDecimal(2)));
        foodInsertDtoJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId + "&isCustom=false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/changeFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodInsertDtoJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Storage storageAfter = storageRepository.findAllByRecordIdAndUserId(validRecordId, user.getId()).get(0);
        assertTrue(storageAfter.getCustomFoods().size() == 1);
        assertTrue(storageAfter.getFoods().size() == 1);
        assertTrue(storageAfter.getFoods().get(foodInsertDto.getName()).getSize()
                .compareTo(new BigDecimal(50)) == 0);
        assertTrue(
                storageAfter.getCustomFoods().get(getApple().getName()).getSize().compareTo(new BigDecimal(100)) == 0);
    }

    @Test
    @Transactional
    public void testDeleteFoodFromStorage_ValidInput_statusNoContent() throws Exception {

        Long validRecordId = 15L;
        User user = getUser(19L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        mapFood(foodInsertDto, getApple());

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodInsertDto);

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

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        mapFood(foodInsertDto, getApple());
        foodInsertDto.setId(validRecordId);

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodInsertDto);

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

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        mapFood(foodInsertDto, getApple());
        foodInsertDto.setId(validRecordId);

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId + "&isCustom=true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson)
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        foodInsertDto.setId(null);
        foodUpdateJson = gson.toJson(foodInsertDto);

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

    @Test
    @Transactional
    public void testDeleteFoodFromStorage_InvalidCustomFoodName_badRequest() throws Exception {

        Long validRecordId = 17L;
        User user = getUser(21L);
        Storage storage = createStorage(validRecordId, user.getId());

        storageRepository.save(storage);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/removeFood?recordId=" + validRecordId
                        + "&foodName=Invalid" + "&isCustom=true")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testGetFoodByStorage_ValidInputNormalFood_statusOk() throws Exception {

        Long validRecordId = 18L;
        User user = getUser(22L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        mapFood(foodInsertDto, getApple());

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .header("X-ViewUser", gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/storage/" + storage.getId() +
                        "/getFood?recordId=" + validRecordId +
                        "&foodName=Apple")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Apple"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calories").value(52))
                .andExpect(MockMvcResultMatchers.jsonPath("$.a").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b1").value(0.017))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b2").value(0.026))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b3").value(0.091))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b5").value(0.043))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b6").value(0.041))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b7").value(0.009))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b9").value(0.003))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b12").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.c").value(0.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calcium").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carbohydrates").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chloride").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chromium").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.copper").value(0.05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.d").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.e").value(0.18))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fat").value(0.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fiber").value(2.4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fluoride").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iodine").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iron").value(0.12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.k").value(0.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.magnesium").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manganese").value(0.035))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monounsaturatedFat").value(0.01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.molybdenum").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phosphorus").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.polyunsaturatedFat").value(0.03))
                .andExpect(MockMvcResultMatchers.jsonPath("$.potassium").value(107))
                .andExpect(MockMvcResultMatchers.jsonPath("$.protein").value(0.3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saturatedFat").value(0.02))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selenium").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sodium").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sugar").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transFat").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zinc").value(0.05));
    }

    @Test
    @Transactional
    public void testGetFoodByStorage_ValidInputCustomFood_statusOk() throws Exception {

        Long validRecordId = 18L;
        User user = getUser(22L);
        Storage storage = createStorage(validRecordId, user.getId());

        FoodInsertDto foodInsertDto = getFoodInsertDto("Apple");
        mapFood(foodInsertDto, getApple());
        foodInsertDto.setId(validRecordId);

        storageRepository.save(storage);

        String foodUpdateJson = gson.toJson(foodInsertDto);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/storage/" + storage.getId() + "/addFood?recordId=" + validRecordId)
                .header("X-ViewUser", gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodUpdateJson))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/storage/" + storage.getId() +
                        "/getFood?recordId=" + validRecordId +
                        "&foodName=Apple" +
                        "&isCustom=true")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Apple"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calories").value(52))
                .andExpect(MockMvcResultMatchers.jsonPath("$.a").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b1").value(0.017))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b2").value(0.026))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b3").value(0.091))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b5").value(0.043))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b6").value(0.041))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b7").value(0.009))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b9").value(0.003))
                .andExpect(MockMvcResultMatchers.jsonPath("$.b12").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.c").value(0.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.calcium").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carbohydrates").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chloride").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chromium").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.copper").value(0.05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.d").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.e").value(0.18))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fat").value(0.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fiber").value(2.4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fluoride").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iodine").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.iron").value(0.12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.k").value(0.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.magnesium").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manganese").value(0.035))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monounsaturatedFat").value(0.01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.molybdenum").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phosphorus").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.polyunsaturatedFat").value(0.03))
                .andExpect(MockMvcResultMatchers.jsonPath("$.potassium").value(107))
                .andExpect(MockMvcResultMatchers.jsonPath("$.protein").value(0.3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saturatedFat").value(0.02))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selenium").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sodium").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sugar").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transFat").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zinc").value(0.05));
    }

    @Test
    @Transactional
    public void testGetFoodByStorage_InvalidStorageIdInput_badRequest() throws Exception {

        Long invalidRecordId = 19L;
        User user = getUser(23L);
        Storage storage = createStorage(invalidRecordId, user.getId());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/storage/" + storage.getId() +
                        "/getFood?recordId=" + invalidRecordId +
                        "&foodName=Apple")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @Transactional
    public void testGetFoodByStorage_InvalidFoodNameInput_badRequest() throws Exception {

        Long validRecordId = 20L;
        User user = getUser(24L);
        Storage storage = createStorage(validRecordId, user.getId());

        storageRepository.save(storage);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/storage/" + storage.getId() +
                        "/getFood?recordId=" + validRecordId +
                        "&foodName=Invalid")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/storage/" + storage.getId() +
                        "/getFood?recordId=" + validRecordId +
                        "&foodName=Invalid" +
                        "&isCustom=true")
                .header("X-ViewUser", gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    private Food getApple() {
        Food food = new Food();
        food.setName("Apple");
        food.setCalories(new BigDecimal("52"));
        food.setSize(new BigDecimal("100"));
        food.setA(new BigDecimal("0"));
        food.setD(new BigDecimal("0"));
        food.setE(new BigDecimal("0.18"));
        food.setK(new BigDecimal("0.5"));
        food.setC(new BigDecimal("0.5"));
        food.setB1(new BigDecimal("0.017"));
        food.setB2(new BigDecimal("0.026"));
        food.setB3(new BigDecimal("0.091"));
        food.setB5(new BigDecimal("0.043"));
        food.setB6(new BigDecimal("0.041"));
        food.setB7(new BigDecimal("0.009"));
        food.setB9(new BigDecimal("0.003"));
        food.setB12(new BigDecimal("0"));
        food.setCalcium(new BigDecimal("6"));
        food.setPhosphorus(new BigDecimal("11"));
        food.setMagnesium(new BigDecimal("5"));
        food.setSodium(new BigDecimal("1"));
        food.setPotassium(new BigDecimal("107"));
        food.setChloride(new BigDecimal("0"));
        food.setIron(new BigDecimal("0.12"));
        food.setZinc(new BigDecimal("0.05"));
        food.setCopper(new BigDecimal("0.05"));
        food.setManganese(new BigDecimal("0.035"));
        food.setIodine(new BigDecimal("0"));
        food.setSelenium(new BigDecimal("0"));
        food.setFluoride(new BigDecimal("0"));
        food.setChromium(new BigDecimal("0"));
        food.setMolybdenum(new BigDecimal("0"));
        food.setCarbohydrates(new BigDecimal("14"));
        food.setProtein(new BigDecimal("0.3"));
        food.setFat(new BigDecimal("0.2"));
        food.setFiber(new BigDecimal("2.4"));
        food.setTransFat(new BigDecimal("0"));
        food.setSaturatedFat(new BigDecimal("0.02"));
        food.setSugar(new BigDecimal("10"));
        food.setPolyunsaturatedFat(new BigDecimal("0.03"));
        food.setMonounsaturatedFat(new BigDecimal("0.01"));
        return food;
    }

    private FoodInsertDto getFoodInsertDto(String name) {
        FoodInsertDto foodInsertDto = new FoodInsertDto();
        foodInsertDto.setName(name);
        return foodInsertDto;
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

    private void mapFood(FoodInsertDto dto, Food food) {
        food.setName(dto.getName());
        dto.setCalories(food.getCalories());
        dto.setSize(food.getSize());
        dto.setA(food.getA());
        dto.setB1(food.getB1());
        dto.setB2(food.getB2());
        dto.setB3(food.getB3());
        dto.setB5(food.getB5());
        dto.setB6(food.getB6());
        dto.setB7(food.getB7());
        dto.setB9(food.getB9());
        dto.setB12(food.getB12());
        dto.setC(food.getC());
        dto.setCalcium(food.getCalcium());
        dto.setCarbohydrates(food.getCarbohydrates());
        dto.setChloride(food.getChloride());
        dto.setChromium(food.getChromium());
        dto.setCopper(food.getCopper());
        dto.setD(food.getD());
        dto.setE(food.getE());
        dto.setFat(food.getFat());
        dto.setFiber(food.getFiber());
        dto.setFluoride(food.getFluoride());
        dto.setIodine(food.getIodine());
        dto.setIron(food.getIron());
        dto.setK(food.getK());
        dto.setMagnesium(food.getMagnesium());
        dto.setManganese(food.getManganese());
        dto.setMonounsaturatedFat(food.getMonounsaturatedFat());
        dto.setMolybdenum(food.getMolybdenum());
        dto.setPhosphorus(food.getPhosphorus());
        dto.setPolyunsaturatedFat(food.getPolyunsaturatedFat());
        dto.setPotassium(food.getPotassium());
        dto.setProtein(food.getProtein());
        dto.setSaturatedFat(food.getSaturatedFat());
        dto.setSelenium(food.getSelenium());
        dto.setSodium(food.getSodium());
        dto.setSugar(food.getSugar());
        dto.setTransFat(food.getTransFat());
        dto.setZinc(food.getZinc());
    }

}
