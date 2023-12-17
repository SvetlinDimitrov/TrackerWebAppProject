package org.storage.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.storage.StorageRepository;
import org.storage.client.User;
import org.storage.exception.FoodException;
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.dto.FoodInsertDto;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Food;
import org.storage.model.entity.Storage;
import org.storage.utils.GsonWrapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final GsonWrapper gsonWrapper;

    public StorageView getStorageByIdAndRecordId(Long storageId, Long recordId, String userToken)
            throws StorageException, InvalidJsonTokenException {
        Long userId = getUserId(userToken);

        return toStorageView(
                storageRepository
                        .findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                        .orElseThrow(() -> new StorageException(
                                "Storage with ID: " + storageId + " not found with record id: " + recordId)));

    }

    public List<StorageView> getAllByRecordId(Long recordId, String userToken) throws InvalidJsonTokenException {

        Long userId = getUserId(userToken);

        List<Storage> storages = storageRepository.findAllByRecordIdAndUserId(recordId, userId);

        if (storages.isEmpty()) {
            return new ArrayList<>();
        }

        return storages
                .stream()
                .map(this::toStorageView)
                .collect(Collectors.toList());

    }

    public void createStorage(Long recordId, String storageName, String userToken) throws InvalidJsonTokenException {
        Long userId = getUserId(userToken);

        Storage storage = new Storage();
        storage.setUserId(userId);
        storage.setRecordId(recordId);
        storage.setConsumedCalories(BigDecimal.ZERO);

        if (storageName == null) {
            storage.setName("Default" + generateRandomNumbers(5));
        } else {
            storage.setName(storageName);
        }

        storageRepository.save(storage);
    }

    @Transactional
    public void deleteStorage(Long recordId, Long storageId, String userToken)
            throws StorageException, InvalidJsonTokenException {

        Long userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId));

        storageRepository.delete(storage);
    }

    @Transactional
    public void deleteAllByRecordIdAndUserId(Long recordId, String userToken) throws InvalidJsonTokenException {
        Long userId = getUserId(userToken);

        storageRepository.deleteAllByRecordIdAndUserId(recordId, userId);
    }

    @Transactional
    public void addFood(Long storageId, Long recordId, FoodInsertDto foodDto, String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException {
        Food food = foodDto.toFood();

        validateFood(food);
        Long userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));

        if (food.getId() == null) {

            if (storage.getFoods().containsKey(food.getName())) {
                Food currentFood = storage.getFoods().get(food.getName());
                combineFoods(currentFood, food);
            } else {
                storage.getFoods().put(food.getName(), food);
            }

        } else {

            if (storage.getCustomFoods().containsKey(food.getName())) {
                Food currentFood = storage.getCustomFoods().get(food.getName());
                combineFoods(currentFood, food);
            } else {
                storage.getCustomFoods().put(food.getName(), food);
            }

        }

        setStorageConsumedCalories(storage);

        storageRepository.save(storage);

    }

    @Transactional
    public void changeFood(Long storageId, Long recordId, FoodInsertDto foodDto, String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException {
        Food food = foodDto.toFood();

        validateFood(food);
        Long userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));

        if (food.getId() == null) {
            if (storage.getFoods().containsKey(food.getName())) {
                storage.getFoods().put(food.getName(), food);
            } else {
                throw new FoodException("Food with name: " + food.getName() + " not found.");
            }
        } else {
            if (storage.getCustomFoods().containsKey(food.getName())) {
                storage.getCustomFoods().put(food.getName(), food);
            } else {
                throw new FoodException("Custom food with name: " + food.getName() + " not found.");
            }
        }

        setStorageConsumedCalories(storage);

        storageRepository.save(storage);

    }

    public void removeFood(Long storageId, Long recordId, String foodName, String userToken, Boolean isCustom)
            throws FoodException, StorageException, InvalidJsonTokenException {

        Long userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));

        if (!isCustom) {
            if (storage.getFoods().containsKey(foodName)) {
                storage.getFoods().remove(foodName);
            } else {
                throw new FoodException("Food with name: " + foodName + " not found.");
            }
        } else {
            if (storage.getCustomFoods().containsKey(foodName)) {
                storage.getCustomFoods().remove(foodName);
            } else {
                throw new FoodException("Custom food with name: " + foodName + " not found.");
            }
        }

        setStorageConsumedCalories(storage);

        storageRepository.save(storage);
    }

    private Food combineFoods(Food food, Food foodToCombine) {

        food.setSize(food.getSize().add(foodToCombine.getSize()));
        food.setCalories(food.getCalories().add(foodToCombine.getCalories()));

        food.setA(food.getA().add(foodToCombine.getA()));
        food.setD(food.getD().add(foodToCombine.getD()));
        food.setE(food.getE().add(foodToCombine.getE()));
        food.setK(food.getK().add(foodToCombine.getK()));
        food.setC(food.getC().add(foodToCombine.getC()));
        food.setB1(food.getB1().add(foodToCombine.getB1()));
        food.setB2(food.getB2().add(foodToCombine.getB2()));
        food.setB3(food.getB3().add(foodToCombine.getB3()));
        food.setB5(food.getB5().add(foodToCombine.getB5()));
        food.setB6(food.getB6().add(foodToCombine.getB6()));
        food.setB7(food.getB7().add(foodToCombine.getB7()));
        food.setB9(food.getB9().add(foodToCombine.getB9()));
        food.setB12(food.getB12().add(foodToCombine.getB12()));

        food.setCalcium(food.getCalcium().add(foodToCombine.getCalcium()));
        food.setPhosphorus(food.getPhosphorus().add(foodToCombine.getPhosphorus()));
        food.setMagnesium(food.getMagnesium().add(foodToCombine.getMagnesium()));
        food.setSodium(food.getSodium().add(foodToCombine.getSodium()));
        food.setPotassium(food.getPotassium().add(foodToCombine.getPotassium()));
        food.setChloride(food.getChloride().add(foodToCombine.getChloride()));
        food.setIron(food.getIron().add(foodToCombine.getIron()));
        food.setZinc(food.getZinc().add(foodToCombine.getZinc()));
        food.setCopper(food.getCopper().add(foodToCombine.getCopper()));
        food.setManganese(food.getManganese().add(foodToCombine.getManganese()));
        food.setIodine(food.getIodine().add(foodToCombine.getIodine()));
        food.setSelenium(food.getSelenium().add(foodToCombine.getSelenium()));
        food.setFluoride(food.getFluoride().add(foodToCombine.getFluoride()));
        food.setChromium(food.getChromium().add(foodToCombine.getChromium()));
        food.setMolybdenum(food.getMolybdenum().add(foodToCombine.getMolybdenum()));

        food.setCarbohydrates(food.getCarbohydrates().add(foodToCombine.getCarbohydrates()));
        food.setProtein(food.getProtein().add(foodToCombine.getProtein()));
        food.setFat(food.getFat().add(foodToCombine.getFat()));

        food.setFiber(food.getFiber().add(foodToCombine.getFiber()));
        food.setTransFat(food.getTransFat().add(foodToCombine.getTransFat()));
        food.setSaturatedFat(food.getSaturatedFat().add(foodToCombine.getSaturatedFat()));
        food.setSugar(food.getSugar().add(foodToCombine.getSugar()));
        food.setPolyunsaturatedFat(food.getPolyunsaturatedFat().add(foodToCombine.getPolyunsaturatedFat()));
        food.setMonounsaturatedFat(food.getMonounsaturatedFat().add(foodToCombine.getMonounsaturatedFat()));

        return food;
    }

    private Long getUserId(String getUserId) throws InvalidJsonTokenException {
        try {
            return gsonWrapper.fromJson(getUserId, User.class).getId();
        } catch (Exception e) {
            throw new InvalidJsonTokenException("Invalid token.");
        }
    }

    private String generateRandomNumbers(int num) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int randomNum = rand.nextInt(100);
            sb.append(randomNum);
        }
        return sb.toString();
    }

    private StorageView toStorageView(Storage entity) {

        StorageView storageView = new StorageView();
        storageView.setId(entity.getId());
        storageView.setRecordId(entity.getRecordId());
        storageView.setName(entity.getName());
        List<Food> foods = new ArrayList<>();
        foods.addAll(entity.getFoods().values());
        foods.addAll(entity.getCustomFoods().values());
        storageView.setFoods(foods);
        storageView.setRecordId(entity.getRecordId());
        storageView.setConsumedCalories(entity.getConsumedCalories());

        return storageView;
    }

    private void setStorageConsumedCalories(Storage storage) {
        BigDecimal consumedCalories = Stream.concat(
                storage.getFoods().values().stream(),
                storage.getCustomFoods().values().stream())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b.getCalories()), BigDecimal::add);

        storage.setConsumedCalories(consumedCalories);
    }

    private void validateFood(Food food) throws FoodException {
        if (food == null || food.getName() == null || food.getName().isEmpty()) {
            throw new FoodException("Food name is required.");
        }
    }

    public Food getFoodByStorage(Long storageId, Long recordId, String foodName, String userToken, Boolean isCustom)
            throws InvalidJsonTokenException, StorageException, FoodException {

        Long userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));

        if (isCustom) {
            if (storage.getCustomFoods().get(foodName) == null) {
                throw new FoodException("Food with name: " + foodName + " not found.");
            }
            return storage.getCustomFoods().get(foodName);
        } else {
            if (storage.getFoods().get(foodName) == null) {
                throw new FoodException("Food with name: " + foodName + " not found.");
            }
            return storage.getFoods().get(foodName);
        }
    }
}
