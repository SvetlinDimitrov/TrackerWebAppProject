package org.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.storage.client.FoodClient;
import org.storage.client.FoodUpdate;
import org.storage.client.User;
import org.storage.exception.FoodException;
import org.storage.exception.StorageException;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Food;
import org.storage.model.entity.Storage;
import org.storage.utils.GsonWrapper;

import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private GsonWrapper gsonWrapper;

    @Mock
    private FoodClient foodClient;

    private ArgumentCaptor<Storage> storageCaptor = ArgumentCaptor.forClass(Storage.class);

    @InjectMocks
    private StorageService storageService;

    @Test
    public void testGetAllByRecordId_ValidInput_ReturnsStorageView() throws StorageException {
        Long recordId = 1L;
        Long userId = 1L;
        String userToken = getUserToken(userId);
        Storage storage = createStorage(recordId, userId);
        StorageView storageView = createStorageView(recordId);

        when(storageRepository.findAllByRecordIdAndUserId(recordId, userId)).thenReturn(List.of(storage));
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        List<StorageView> result = storageService.getAllByRecordId(recordId, userToken);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getName(), storageView.getName());
    }

    @Test
    public void testGetAllByRecordId_InvalidInput_throwsStorageException() throws StorageException {
        Long recordId = 2L;
        Long userId = 2L;
        String userToken = getUserToken(userId);

        when(storageRepository.findAllByRecordIdAndUserId(recordId, userId)).thenReturn(List.of());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        assertThrows(StorageException.class, () -> storageService.getAllByRecordId(recordId, userToken));
    }

    @Test
    public void testGetStorageByIdAndRecordId_ValidInput_ReturnsStorageView() throws StorageException {

        Long recordId = 3L;
        Long userId = 3L;
        String userToken = getUserToken(userId);
        Storage storage = createStorage(recordId, userId);
        StorageView storageView = createStorageView(recordId);

        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.ofNullable(storage));

        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        StorageView result = storageService.getStorageByIdAndRecordId(storage.getId(), recordId, userToken);

        assertEquals(result.getName(), storageView.getName());
    }

    @Test
    public void testGetStorageByIdAndRecordId_InvalidInput_throwsStorageException() throws StorageException {

        Long recordId = 4L;
        Long userId = 4L;
        String userToken = getUserToken(userId);
        Storage storage = createStorage(recordId, userId);

        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.empty());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        assertThrows(StorageException.class,
                () -> storageService.getStorageByIdAndRecordId(storage.getId(), recordId, userToken));
    }

    @Test
    public void testCreateStorage_NullStorageName_ReturnsDefaultStorageName() throws StorageException {

        Long recordId = 5L;
        Long userId = 5L;
        String userToken = getUserToken(userId);

        Mockito.when(storageRepository.save(storageCaptor.capture())).thenReturn(new Storage());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        storageService.createStorage(recordId, null, userToken);

        Storage capturedStorage = storageCaptor.getValue();

        assertTrue(capturedStorage.getName().contains("Default"));
    }

    @Test
    public void testCreateStorage_ProvidedStorageName_StorageNameSavedCorrectly() throws StorageException {
        Long recordId = 6L;
        Long userId = 6L;
        String userToken = getUserToken(userId);
        Mockito.when(storageRepository.save(storageCaptor.capture())).thenReturn(new Storage());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        storageService.createStorage(recordId, "Ivan's Storage", userToken);

        Storage capturedStorage = storageCaptor.getValue();

        assertTrue(capturedStorage.getName().equals("Ivan's Storage"));
    }

    @Test
    public void firstCreationStorage_assertFourStorages() throws StorageException {
        Long recordId = 7L;
        Long userId = 7L;
        String userToken = getUserToken(userId);

        Mockito.when(storageRepository.save(storageCaptor.capture())).thenReturn(new Storage());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        storageService.firstCreationStorage(recordId, userToken);

        Mockito.verify(storageRepository, Mockito.times(4)).save(storageCaptor.capture());
    }

    @Test
    public void testDeleteStorage_invalidInput_throwsStorageException() {
        Long recordId = 8L;
        Long userId = 8L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        when(storageRepository
                .findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.empty());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        assertThrows(StorageException.class, () -> storageService.deleteStorage(recordId, storage.getId(), userToken));
    }

    @Test
    public void testDeleteStorage_validInput() throws StorageException {
        Long recordId = 9L;
        Long userId = 9L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        when(storageRepository
                .findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));

        storageService.deleteStorage(recordId, storage.getId(), userToken);

        Mockito.verify(storageRepository, Mockito.times(1)).delete(storage);
    }

    @Test
    public void testDeleteAllByRecordId_validInput() {
        Long recordId = 10L;
        Long userId = 10L;
        String userToken = getUserToken(userId);

        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        storageService.deleteAllByRecordIdAndUserId(recordId, userToken);

        verify(storageRepository, Mockito.times(1)).deleteAllByRecordIdAndUserId(recordId, userId);
    }

    @Test
    public void testAddFoodToStorage_singleFoodAdded_AddedCorrectlyFood() throws StorageException, FoodException {
        Long recordId = 11L;
        Long userId = 11L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        Food food = getApple();

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName(food.getName());

        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));
        when(foodClient.getFoodByName(food.getName())).thenReturn(food);

        storageService.addFood(storage.getId(), recordId, foodUpdate, userToken);

        verify(storageRepository, Mockito.times(1)).save(storageCaptor.capture());

        Storage capturedStorage = storageCaptor.getValue();
        Food resultedFood = capturedStorage.getFoods().get(food.getName());

        assertTrue(capturedStorage.getFoods().containsKey(food.getName()));

        assertTrue(capturedStorage.getConsumedCalories().compareTo(food.getCalories()) == 0);
        assertTrue(getApple().getA().compareTo(resultedFood.getA()) == 0);
        assertTrue(getApple().getB1().compareTo(resultedFood.getB1()) == 0);
        assertTrue(getApple().getB12().compareTo(resultedFood.getB12()) == 0);
        assertTrue(getApple().getB2().compareTo(resultedFood.getB2()) == 0);
        assertTrue(getApple().getB3().compareTo(resultedFood.getB3()) == 0);
        assertTrue(getApple().getB5().compareTo(resultedFood.getB5()) == 0);
        assertTrue(getApple().getB6().compareTo(resultedFood.getB6()) == 0);
        assertTrue(getApple().getB7().compareTo(resultedFood.getB7()) == 0);
        assertTrue(getApple().getB9().compareTo(resultedFood.getB9()) == 0);
        assertTrue(getApple().getC().compareTo(resultedFood.getC()) == 0);
        assertTrue(getApple().getD().compareTo(resultedFood.getD()) == 0);
        assertTrue(getApple().getE().compareTo(resultedFood.getE()) == 0);
        assertTrue(getApple().getK().compareTo(resultedFood.getK()) == 0);

        assertTrue(getApple().getCalcium().compareTo(resultedFood.getCalcium()) == 0);
        assertTrue(getApple().getCalories().compareTo(resultedFood.getCalories()) == 0);
        assertTrue(getApple().getChloride().compareTo(resultedFood.getChloride()) == 0);
        assertTrue(getApple().getChromium().compareTo(resultedFood.getChromium()) == 0);
        assertTrue(getApple().getCopper().compareTo(resultedFood.getCopper()) == 0);
        assertTrue(getApple().getFiber().compareTo(resultedFood.getFiber()) == 0);
        assertTrue(getApple().getFluoride().compareTo(resultedFood.getFluoride()) == 0);
        assertTrue(getApple().getIron().compareTo(resultedFood.getIron()) == 0);
        assertTrue(getApple().getIodine().compareTo(resultedFood.getIodine()) == 0);
        assertTrue(getApple().getMagnesium().compareTo(resultedFood.getMagnesium()) == 0);
        assertTrue(getApple().getManganese().compareTo(resultedFood.getManganese()) == 0);
        assertTrue(getApple().getMolybdenum().compareTo(resultedFood.getMolybdenum()) == 0);
        assertTrue(getApple().getPhosphorus().compareTo(resultedFood.getPhosphorus()) == 0);
        assertTrue(getApple().getPotassium().compareTo(resultedFood.getPotassium()) == 0);
        assertTrue(getApple().getSelenium().compareTo(resultedFood.getSelenium()) == 0);
        assertTrue(getApple().getSodium().compareTo(resultedFood.getSodium()) == 0);
        assertTrue(getApple().getTransFat().compareTo(resultedFood.getTransFat()) == 0);
        assertTrue(getApple().getZinc().compareTo(resultedFood.getZinc()) == 0);

        assertTrue(getApple().getCarbohydrates().compareTo(resultedFood.getCarbohydrates()) == 0);
        assertTrue(getApple().getFat().compareTo(resultedFood.getFat()) == 0);
        assertTrue(getApple().getMonounsaturatedFat().compareTo(resultedFood.getMonounsaturatedFat()) == 0);
        assertTrue(getApple().getPolyunsaturatedFat().compareTo(resultedFood.getPolyunsaturatedFat()) == 0);
        assertTrue(getApple().getProtein().compareTo(resultedFood.getProtein()) == 0);
        assertTrue(getApple().getSaturatedFat().compareTo(resultedFood.getSaturatedFat()) == 0);
        assertTrue(getApple().getSugar().compareTo(resultedFood.getSugar()) == 0);
    }

    @Test
    public void testAddFoodToStorage_singleFoodAddedDifferentSize_AddedCorrectlyFood()
            throws StorageException, FoodException {
        Long recordId = 12L;
        Long userId = 12L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        Food food = getApple();

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(50));
        foodUpdate.setFoodName(food.getName());

        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));
        when(foodClient.getFoodByName(food.getName())).thenReturn(food);

        storageService.addFood(storage.getId(), recordId, foodUpdate, userToken);

        verify(storageRepository, Mockito.times(1)).save(storageCaptor.capture());

        Storage capturedStorage = storageCaptor.getValue();

        assertTrue(capturedStorage.getFoods().containsKey(food.getName()));
        assertTrue(capturedStorage.getConsumedCalories().compareTo(food.getCalories().divide(new BigDecimal(2))) == 0);

    }

    @Test
    public void testAddFoodToStorage_TheSameFoodAddedMultiTimes_AddedCorrectlyFood()
            throws StorageException, FoodException {
        Long recordId = 13L;
        Long userId = 13L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        Food food = getApple();
        BigDecimal expectedCaloriesFirstTime = food.getCalories().divide(new BigDecimal(2));
        BigDecimal expectedCaloriesSecondTime = expectedCaloriesFirstTime.add(getApple().getCalories());

        storage.getFoods().put(food.getName(), food);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(50));
        foodUpdate.setFoodName(food.getName());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));
        when(foodClient.getFoodByName(food.getName())).thenReturn(food);

        storageService.addFood(storage.getId(), recordId, foodUpdate, userToken);

        verify(storageRepository, Mockito.times(1)).save(storageCaptor.capture());

        Storage capturedStorage = storageCaptor.getValue();
        Food resultedFood = capturedStorage.getFoods().get(food.getName());

        assertTrue(capturedStorage.getFoods().containsKey(food.getName()));
        assertTrue(capturedStorage.getConsumedCalories().compareTo(expectedCaloriesSecondTime) == 0);

        assertTrue(getIncreaseTransformedValue(1.5, getApple().getA()).compareTo(resultedFood.getA()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB1()).compareTo(resultedFood.getB1()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB12()).compareTo(resultedFood.getB12()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB2()).compareTo(resultedFood.getB2()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB3()).compareTo(resultedFood.getB3()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB5()).compareTo(resultedFood.getB5()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB6()).compareTo(resultedFood.getB6()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB7()).compareTo(resultedFood.getB7()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getB9()).compareTo(resultedFood.getB9()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getC()).compareTo(resultedFood.getC()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getD()).compareTo(resultedFood.getD()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getE()).compareTo(resultedFood.getE()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getK()).compareTo(resultedFood.getK()) == 0);

        assertTrue(getIncreaseTransformedValue(1.5, getApple().getCalcium()).compareTo(resultedFood.getCalcium()) == 0);
        assertTrue(
                getIncreaseTransformedValue(1.5, getApple().getCalories()).compareTo(resultedFood.getCalories()) == 0);
        assertTrue(
                getIncreaseTransformedValue(1.5, getApple().getChloride()).compareTo(resultedFood.getChloride()) == 0);
        assertTrue(
                getIncreaseTransformedValue(1.5, getApple().getChromium()).compareTo(resultedFood.getChromium()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getCopper()).compareTo(resultedFood.getCopper()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getFiber()).compareTo(resultedFood.getFiber()) == 0);
        assertTrue(
                getIncreaseTransformedValue(1.5, getApple().getFluoride()).compareTo(resultedFood.getFluoride()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getIron()).compareTo(resultedFood.getIron()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getIodine()).compareTo(resultedFood.getIodine()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getMagnesium())
                .compareTo(resultedFood.getMagnesium()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getManganese())
                .compareTo(resultedFood.getManganese()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getMolybdenum())
                .compareTo(resultedFood.getMolybdenum()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getPhosphorus())
                .compareTo(resultedFood.getPhosphorus()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getPotassium())
                .compareTo(resultedFood.getPotassium()) == 0);
        assertTrue(
                getIncreaseTransformedValue(1.5, getApple().getSelenium()).compareTo(resultedFood.getSelenium()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getSodium()).compareTo(resultedFood.getSodium()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getZinc()).compareTo(resultedFood.getZinc()) == 0);

        assertTrue(getIncreaseTransformedValue(1.5, getApple().getCarbohydrates())
                .compareTo(resultedFood.getCarbohydrates()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getFat()).compareTo(resultedFood.getFat()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getMonounsaturatedFat())
                .compareTo(resultedFood.getMonounsaturatedFat()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getPolyunsaturatedFat())
                .compareTo(resultedFood.getPolyunsaturatedFat()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getProtein()).compareTo(resultedFood.getProtein()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getSaturatedFat())
                .compareTo(resultedFood.getSaturatedFat()) == 0);
        assertTrue(getIncreaseTransformedValue(1.5, getApple().getSugar()).compareTo(resultedFood.getSugar()) == 0);
        assertTrue(
                getIncreaseTransformedValue(1.5, getApple().getTransFat()).compareTo(resultedFood.getTransFat()) == 0);
    }

    @Test
    public void testAddFoodToStorage_invalidInput_throwsStorageException() {
        Long recordId = 14L;
        Long userId = 14L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.empty());

        assertThrows(StorageException.class,
                () -> storageService.addFood(storage.getId(), recordId, foodUpdate, userToken));
    }

    @Test
    public void testAddFoodToStorage_invalidInput_throwsFoodException() {
        Long recordId = 15L;
        Long userId = 15L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));
        when(foodClient.getFoodByName(foodUpdate.getFoodName())).thenThrow(new RuntimeException("Invalid food name"));

        assertThrows(FoodException.class,
                () -> storageService.addFood(storage.getId(), recordId, foodUpdate, userToken));
    }

    @Test
    public void testAddFoodToStorage_invalidInputAmount_throwsFoodException2() {
        Long recordId = 16L;
        Long userId = 16L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(-100));
        foodUpdate.setFoodName("Apple");
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        assertThrows(FoodException.class,
                () -> storageService.addFood(storage.getId(), recordId, foodUpdate, userToken));
    }

    @Test
    public void testChangeFood_WithEqualMeasurement_NothingChanges() throws StorageException, FoodException {
        Long recordId = 17L;
        Long userId = 17L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        Food food = getApple();
        storage.getFoods().put(food.getName(), food);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName(food.getName());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        storageService.changeFood(storage.getId(), recordId, foodUpdate, userToken);

        verify(storageRepository, Mockito.times(1)).save(storageCaptor.capture());

        Storage capturedStorage = storageCaptor.getValue();
        Food resultedFood = capturedStorage.getFoods().get(food.getName());

        assertTrue(capturedStorage.getFoods().containsKey(food.getName()));
        assertTrue(capturedStorage.getFoods().get((food.getName())).getSize().compareTo(foodUpdate.getAmount()) == 0);
        assertTrue(capturedStorage.getConsumedCalories().compareTo(food.getCalories()) == 0);

        assertTrue(getApple().getA().compareTo(resultedFood.getA()) == 0);
        assertTrue(getApple().getB1().compareTo(resultedFood.getB1()) == 0);
        assertTrue(getApple().getB12().compareTo(resultedFood.getB12()) == 0);
        assertTrue(getApple().getB2().compareTo(resultedFood.getB2()) == 0);
        assertTrue(getApple().getB3().compareTo(resultedFood.getB3()) == 0);
        assertTrue(getApple().getB5().compareTo(resultedFood.getB5()) == 0);
        assertTrue(getApple().getB6().compareTo(resultedFood.getB6()) == 0);
        assertTrue(getApple().getB7().compareTo(resultedFood.getB7()) == 0);
        assertTrue(getApple().getB9().compareTo(resultedFood.getB9()) == 0);
        assertTrue(getApple().getC().compareTo(resultedFood.getC()) == 0);
        assertTrue(getApple().getD().compareTo(resultedFood.getD()) == 0);
        assertTrue(getApple().getE().compareTo(resultedFood.getE()) == 0);
        assertTrue(getApple().getK().compareTo(resultedFood.getK()) == 0);

        assertTrue(getApple().getCalcium().compareTo(resultedFood.getCalcium()) == 0);
        assertTrue(getApple().getCalories().compareTo(resultedFood.getCalories()) == 0);
        assertTrue(getApple().getChloride().compareTo(resultedFood.getChloride()) == 0);
        assertTrue(getApple().getChromium().compareTo(resultedFood.getChromium()) == 0);
        assertTrue(getApple().getCopper().compareTo(resultedFood.getCopper()) == 0);
        assertTrue(getApple().getFiber().compareTo(resultedFood.getFiber()) == 0);
        assertTrue(getApple().getFluoride().compareTo(resultedFood.getFluoride()) == 0);
        assertTrue(getApple().getIron().compareTo(resultedFood.getIron()) == 0);
        assertTrue(getApple().getIodine().compareTo(resultedFood.getIodine()) == 0);
        assertTrue(getApple().getMagnesium().compareTo(resultedFood.getMagnesium()) == 0);
        assertTrue(getApple().getManganese().compareTo(resultedFood.getManganese()) == 0);
        assertTrue(getApple().getMolybdenum().compareTo(resultedFood.getMolybdenum()) == 0);
        assertTrue(getApple().getPhosphorus().compareTo(resultedFood.getPhosphorus()) == 0);
        assertTrue(getApple().getPotassium().compareTo(resultedFood.getPotassium()) == 0);
        assertTrue(getApple().getSelenium().compareTo(resultedFood.getSelenium()) == 0);
        assertTrue(getApple().getSodium().compareTo(resultedFood.getSodium()) == 0);
        assertTrue(getApple().getTransFat().compareTo(resultedFood.getTransFat()) == 0);
        assertTrue(getApple().getZinc().compareTo(resultedFood.getZinc()) == 0);

        assertTrue(getApple().getCarbohydrates().compareTo(resultedFood.getCarbohydrates()) == 0);
        assertTrue(getApple().getFat().compareTo(resultedFood.getFat()) == 0);
        assertTrue(getApple().getMonounsaturatedFat().compareTo(resultedFood.getMonounsaturatedFat()) == 0);
        assertTrue(getApple().getPolyunsaturatedFat().compareTo(resultedFood.getPolyunsaturatedFat()) == 0);
        assertTrue(getApple().getProtein().compareTo(resultedFood.getProtein()) == 0);
        assertTrue(getApple().getSaturatedFat().compareTo(resultedFood.getSaturatedFat()) == 0);
        assertTrue(getApple().getSugar().compareTo(resultedFood.getSugar()) == 0);
    }

    @Test
    public void testChangeFood_WithDifferentHigherMeasurement_ChangesCorrectly()
            throws StorageException, FoodException {
        Long recordId = 18L;
        Long userId = 18L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        Food food = getApple();
        storage.getFoods().put(food.getName(), divideAppleStats(food, new BigDecimal(2)));

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName(food.getName());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        storageService.changeFood(storage.getId(), recordId, foodUpdate, userToken);

        verify(storageRepository, Mockito.times(1)).save(storageCaptor.capture());

        Storage capturedStorage = storageCaptor.getValue();
        Food resultedFood = capturedStorage.getFoods().get(food.getName());

        assertTrue(capturedStorage.getFoods().containsKey(food.getName()));
        assertTrue(capturedStorage.getFoods().get((food.getName())).getSize().compareTo(foodUpdate.getAmount()) == 0);
        assertTrue(capturedStorage.getConsumedCalories().compareTo(food.getCalories()) == 0);

        assertTrue(getApple().getA().compareTo(resultedFood.getA()) == 0);
        assertTrue(getApple().getB1().compareTo(resultedFood.getB1()) == 0);
        assertTrue(getApple().getB12().compareTo(resultedFood.getB12()) == 0);
        assertTrue(getApple().getB2().compareTo(resultedFood.getB2()) == 0);
        assertTrue(getApple().getB3().compareTo(resultedFood.getB3()) == 0);
        assertTrue(getApple().getB5().compareTo(resultedFood.getB5()) == 0);
        assertTrue(getApple().getB6().compareTo(resultedFood.getB6()) == 0);
        assertTrue(getApple().getB7().compareTo(resultedFood.getB7()) == 0);
        assertTrue(getApple().getB9().compareTo(resultedFood.getB9()) == 0);
        assertTrue(getApple().getC().compareTo(resultedFood.getC()) == 0);
        assertTrue(getApple().getD().compareTo(resultedFood.getD()) == 0);
        assertTrue(getApple().getE().compareTo(resultedFood.getE()) == 0);
        assertTrue(getApple().getK().compareTo(resultedFood.getK()) == 0);

        assertTrue(getApple().getCalcium().compareTo(resultedFood.getCalcium()) == 0);
        assertTrue(getApple().getCalories().compareTo(resultedFood.getCalories()) == 0);
        assertTrue(getApple().getChloride().compareTo(resultedFood.getChloride()) == 0);
        assertTrue(getApple().getChromium().compareTo(resultedFood.getChromium()) == 0);
        assertTrue(getApple().getCopper().compareTo(resultedFood.getCopper()) == 0);
        assertTrue(getApple().getFiber().compareTo(resultedFood.getFiber()) == 0);
        assertTrue(getApple().getFluoride().compareTo(resultedFood.getFluoride()) == 0);
        assertTrue(getApple().getIron().compareTo(resultedFood.getIron()) == 0);
        assertTrue(getApple().getIodine().compareTo(resultedFood.getIodine()) == 0);
        assertTrue(getApple().getMagnesium().compareTo(resultedFood.getMagnesium()) == 0);
        assertTrue(getApple().getManganese().compareTo(resultedFood.getManganese()) == 0);
        assertTrue(getApple().getMolybdenum().compareTo(resultedFood.getMolybdenum()) == 0);
        assertTrue(getApple().getPhosphorus().compareTo(resultedFood.getPhosphorus()) == 0);
        assertTrue(getApple().getPotassium().compareTo(resultedFood.getPotassium()) == 0);
        assertTrue(getApple().getSelenium().compareTo(resultedFood.getSelenium()) == 0);
        assertTrue(getApple().getSodium().compareTo(resultedFood.getSodium()) == 0);
        assertTrue(getApple().getTransFat().compareTo(resultedFood.getTransFat()) == 0);
        assertTrue(getApple().getZinc().compareTo(resultedFood.getZinc()) == 0);

        assertTrue(getApple().getCarbohydrates().compareTo(resultedFood.getCarbohydrates()) == 0);
        assertTrue(getApple().getFat().compareTo(resultedFood.getFat()) == 0);
        assertTrue(getApple().getMonounsaturatedFat().compareTo(resultedFood.getMonounsaturatedFat()) == 0);
        assertTrue(getApple().getPolyunsaturatedFat().compareTo(resultedFood.getPolyunsaturatedFat()) == 0);
        assertTrue(getApple().getProtein().compareTo(resultedFood.getProtein()) == 0);
        assertTrue(getApple().getSaturatedFat().compareTo(resultedFood.getSaturatedFat()) == 0);
        assertTrue(getApple().getSugar().compareTo(resultedFood.getSugar()) == 0);
    }

    @Test
    public void testChangeFood_WithDifferentLowerMeasurement_ChangesCorrectly() throws StorageException, FoodException {
        Long recordId = 19L;
        Long userId = 19L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        Food food = getApple();
        storage.getFoods().put(food.getName(), food);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(50));
        foodUpdate.setFoodName(food.getName());
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        storageService.changeFood(storage.getId(), recordId, foodUpdate, userToken);

        verify(storageRepository, Mockito.times(1)).save(storageCaptor.capture());

        Storage capturedStorage = storageCaptor.getValue();
        Food resultedFood = capturedStorage.getFoods().get(food.getName());

        assertTrue(capturedStorage.getFoods().containsKey(food.getName()));
        assertTrue(capturedStorage.getFoods().get((food.getName())).getSize().compareTo(foodUpdate.getAmount()) == 0);
        assertTrue(capturedStorage.getConsumedCalories().compareTo(food.getCalories().divide(new BigDecimal(2))) == 0);

        assertTrue(getDecreesTransformedValue(getApple().getA(), 2).compareTo(resultedFood.getA()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB1(), 2).compareTo(resultedFood.getB1()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB12(), 2).compareTo(resultedFood.getB12()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB2(), 2).compareTo(resultedFood.getB2()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB3(), 2).compareTo(resultedFood.getB3()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB5(), 2).compareTo(resultedFood.getB5()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB6(), 2).compareTo(resultedFood.getB6()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB7(), 2).compareTo(resultedFood.getB7()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getB9(), 2).compareTo(resultedFood.getB9()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getC(), 2).compareTo(resultedFood.getC()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getD(), 2).compareTo(resultedFood.getD()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getE(), 2).compareTo(resultedFood.getE()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getK(), 2).compareTo(resultedFood.getK()) == 0);

        assertTrue(getDecreesTransformedValue(getApple().getCalcium(), 2).compareTo(resultedFood.getCalcium()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getCalories(), 2).compareTo(resultedFood.getCalories()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getChloride(), 2).compareTo(resultedFood.getChloride()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getChromium(), 2).compareTo(resultedFood.getChromium()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getCopper(), 2).compareTo(resultedFood.getCopper()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getFiber(), 2).compareTo(resultedFood.getFiber()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getFluoride(), 2).compareTo(resultedFood.getFluoride()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getIron(), 2).compareTo(resultedFood.getIron()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getIodine(), 2).compareTo(resultedFood.getIodine()) == 0);
        assertTrue(
                getDecreesTransformedValue(getApple().getMagnesium(), 2).compareTo(resultedFood.getMagnesium()) == 0);
        assertTrue(
                getDecreesTransformedValue(getApple().getManganese(), 2).compareTo(resultedFood.getManganese()) == 0);
        assertTrue(
                getDecreesTransformedValue(getApple().getMolybdenum(), 2).compareTo(resultedFood.getMolybdenum()) == 0);
        assertTrue(
                getDecreesTransformedValue(getApple().getPhosphorus(), 2).compareTo(resultedFood.getPhosphorus()) == 0);
        assertTrue(
                getDecreesTransformedValue(getApple().getPotassium(), 2).compareTo(resultedFood.getPotassium()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getSelenium(), 2).compareTo(resultedFood.getSelenium()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getSodium(), 2).compareTo(resultedFood.getSodium()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getTransFat(), 2).compareTo(resultedFood.getTransFat()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getZinc(), 2).compareTo(resultedFood.getZinc()) == 0);

        assertTrue(getDecreesTransformedValue(getApple().getCarbohydrates(), 2)
                .compareTo(resultedFood.getCarbohydrates()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getFat(), 2).compareTo(resultedFood.getFat()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getMonounsaturatedFat(), 2)
                .compareTo(resultedFood.getMonounsaturatedFat()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getPolyunsaturatedFat(), 2)
                .compareTo(resultedFood.getPolyunsaturatedFat()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getProtein(), 2).compareTo(resultedFood.getProtein()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getSaturatedFat(), 2)
                .compareTo(resultedFood.getSaturatedFat()) == 0);
        assertTrue(getDecreesTransformedValue(getApple().getSugar(), 2).compareTo(resultedFood.getSugar()) == 0);
    }

    @Test
    public void testChangeFood_InvalidStorageId_throwsStorageException() {
        Long recordId = 20L;
        Long userId = 20L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Apple");
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.empty());

        assertThrows(StorageException.class,
                () -> storageService.changeFood(storage.getId(), recordId, foodUpdate, userToken));
    }

    @Test
    public void testChangeFood_InvalidFoodName_throwsFoodException() {
        Long recordId = 21L;
        Long userId = 21L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(100));
        foodUpdate.setFoodName("Invalid food name");
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        assertThrows(FoodException.class,
                () -> storageService.changeFood(storage.getId(), recordId, foodUpdate, userToken));
    }

    @Test
    public void testChangeFood_InvalidAmount_throwsFoodException() {
        Long recordId = 22L;
        Long userId = 22L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);

        FoodUpdate foodUpdate = new FoodUpdate();
        foodUpdate.setAmount(new BigDecimal(-100));
        foodUpdate.setFoodName("Apple");
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        assertThrows(FoodException.class,
                () -> storageService.changeFood(storage.getId(), recordId, foodUpdate, userToken));
    }

    @Test
    public void testRemoveFood_validInput() throws StorageException, FoodException {
        Long recordId = 23L;
        Long userId = 23L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        Food food = getApple();
        storage.getFoods().put(food.getName(), food);
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        storageService.removeFood(storage.getId(), recordId, food.getName(), userToken);

        verify(storageRepository, Mockito.times(1)).save(storageCaptor.capture());

        Storage capturedStorage = storageCaptor.getValue();

        assertTrue(capturedStorage.getFoods().isEmpty());
        assertTrue(capturedStorage.getConsumedCalories().compareTo(new BigDecimal(0)) == 0);
    }

    @Test
    public void testRemoveFood_invalidStorageInput_throwsStorageException() {
        Long recordId = 24L;
        Long userId = 24L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.empty());

        assertThrows(StorageException.class,
                () -> storageService.removeFood(storage.getId(), recordId, "Apple", userToken));
    }

    @Test
    public void testRemoveFood_invalidFoodInput_throwsFoodException() {
        Long recordId = 25L;
        Long userId = 25L;
        String userToken = getUserToken(userId);

        Storage storage = createStorage(recordId, userId);
        when(gsonWrapper.fromJson(userToken, User.class)).thenReturn(new User(userId));
        when(storageRepository.findByIdAndRecordIdAndUserId(storage.getId(), recordId, userId))
                .thenReturn(Optional.of(storage));

        assertThrows(FoodException.class,
                () -> storageService.removeFood(storage.getId(), recordId, "Invalid food name", userToken));
    }

    private Storage createStorage(Long recordId, Long userId) {
        Storage entity = new Storage();
        entity.setName("test");
        entity.setRecordId(recordId);
        entity.setUserId(userId);
        entity.setFoods(new HashMap<>());
        return entity;
    }

    private StorageView createStorageView(Long recordId) {
        StorageView view = new StorageView();
        view.setName("test");
        view.setRecordId(recordId);
        view.setFoods(List.of());
        return view;
    }

    private Food getApple() {
        return Food.builder()
                .size(new BigDecimal(100))
                .name("Apple")
                .calories(new BigDecimal("52"))
                .A(new BigDecimal("0.0"))
                .D(new BigDecimal("0.0"))
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
                .B12(new BigDecimal("0.0"))
                .Calcium(new BigDecimal("6"))
                .Phosphorus(new BigDecimal("11"))
                .Magnesium(new BigDecimal("5"))
                .Sodium(new BigDecimal("1"))
                .Potassium(new BigDecimal("107"))
                .Chloride(new BigDecimal("0.0"))
                .Iron(new BigDecimal("0.12"))
                .Zinc(new BigDecimal("0.05"))
                .Copper(new BigDecimal("0.05"))
                .Manganese(new BigDecimal("0.035"))
                .Iodine(new BigDecimal("0.0"))
                .Selenium(new BigDecimal("0.0"))
                .Fluoride(new BigDecimal("0.0"))
                .Chromium(new BigDecimal("0.0"))
                .Molybdenum(new BigDecimal("0.0"))
                .Carbohydrates(new BigDecimal("14"))
                .Protein(new BigDecimal("0.3"))
                .Fat(new BigDecimal("0.2"))
                .Fiber(new BigDecimal("2.4"))
                .TransFat(new BigDecimal("0.0"))
                .SaturatedFat(new BigDecimal("0.02"))
                .Sugar(new BigDecimal("10"))
                .PolyunsaturatedFat(new BigDecimal("0.03"))
                .MonounsaturatedFat(new BigDecimal("0.01"))
                .build();
    }

    private Food divideAppleStats(Food food, BigDecimal divider) {
        return Food.builder()
                .size(food.getSize().divide(divider))
                .name(food.getName())
                .calories(food.getCalories().divide(divider))
                .A(food.getA().divide(divider))
                .D(food.getD().divide(divider))
                .E(food.getE().divide(divider))
                .K(food.getK().divide(divider))
                .C(food.getC().divide(divider))
                .B1(food.getB1().divide(divider))
                .B2(food.getB2().divide(divider))
                .B3(food.getB3().divide(divider))
                .B5(food.getB5().divide(divider))
                .B6(food.getB6().divide(divider))
                .B7(food.getB7().divide(divider))
                .B9(food.getB9().divide(divider))
                .B12(food.getB12().divide(divider))
                .Calcium(food.getCalcium().divide(divider))
                .Phosphorus(food.getPhosphorus().divide(divider))
                .Magnesium(food.getMagnesium().divide(divider))
                .Sodium(food.getSodium().divide(divider))
                .Potassium(food.getPotassium().divide(divider))
                .Chloride(food.getChloride().divide(divider))
                .Iron(food.getIron().divide(divider))
                .Zinc(food.getZinc().divide(divider))
                .Copper(food.getCopper().divide(divider))
                .Manganese(food.getManganese().divide(divider))
                .Iodine(food.getIodine().divide(divider))
                .Selenium(food.getSelenium().divide(divider))
                .Fluoride(food.getFluoride().divide(divider))
                .Chromium(food.getChromium().divide(divider))
                .Molybdenum(food.getMolybdenum().divide(divider))
                .Carbohydrates(food.getCarbohydrates().divide(divider))
                .Protein(food.getProtein().divide(divider))
                .Fat(food.getFat().divide(divider))
                .Fiber(food.getFiber().divide(divider))
                .TransFat(food.getTransFat().divide(divider))
                .SaturatedFat(food.getSaturatedFat().divide(divider))
                .Sugar(food.getSugar().divide(divider))
                .PolyunsaturatedFat(food.getPolyunsaturatedFat().divide(divider))
                .MonounsaturatedFat(food.getMonounsaturatedFat().divide(divider))
                .build();
    }

    private BigDecimal getIncreaseTransformedValue(Double num, BigDecimal bigDecimal) {
        return bigDecimal.multiply(new BigDecimal(num));
    }

    private BigDecimal getDecreesTransformedValue(BigDecimal num, Integer num2) {
        return num.divide(new BigDecimal(num2), 2, RoundingMode.HALF_UP);
    }

    private String getUserToken(Long userId) {
        User user = new User();
        user.setId(userId);
        Gson gson = new Gson();

        return gson.toJson(user);
    }
}
