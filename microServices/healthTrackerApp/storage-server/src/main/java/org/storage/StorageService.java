package org.storage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.storage.client.FoodClient;
import org.storage.client.FoodUpdate;
import org.storage.client.User;
import org.storage.exception.FoodException;
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Food;
import org.storage.model.entity.Storage;
import org.storage.utils.GsonWrapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final FoodClient foodClient;
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
           return  new ArrayList<>();
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

    public void firstCreationStorage(Long recordId, String userToken) throws InvalidJsonTokenException {
        Long userId = getUserId(userToken);

        Storage storage = new Storage("First Meal", recordId, userId);
        Storage storage2 = new Storage("Second Meal", recordId, userId);
        Storage storage3 = new Storage("Third Meal", recordId, userId);
        Storage storage4 = new Storage("Snacks", recordId, userId);

        storageRepository.save(storage);
        storageRepository.save(storage2);
        storageRepository.save(storage3);
        storageRepository.save(storage4);
    }

    @Transactional
    public void deleteStorage(Long recordId, Long storageId, String userToken) throws StorageException, InvalidJsonTokenException {

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

    public void addFood(Long storageId, Long recordId, FoodUpdate foodInfo, String userToken, Boolean isCustom)
        throws StorageException, FoodException, InvalidJsonTokenException {

        Long userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));
        
        if (foodInfo.getFoodName() == null || foodInfo.getFoodName().isEmpty()) {
            throw new FoodException("Food name cannot be empty.");
        }
        if ( foodInfo.getAmount() != null && foodInfo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FoodException("Amount must be greater than 0.");
        }
        try {
            Food food = null;
            if (!isCustom) {
                food = foodClient.getFoodByName(foodInfo.getFoodName());
            } else {
                food = foodClient.getCustomFoodByNameAndSize(foodInfo.getFoodName(), foodInfo.getAmount().doubleValue(),
                        userToken);
            }

            if (!isCustom) {

                if (storage.getFoods().containsKey(foodInfo.getFoodName())) {
                    Food currentFood = storage.getFoods().get(foodInfo.getFoodName());
                    combineFoods(currentFood, calculateFoodToAdd(food, foodInfo.getAmount()));
                } else {
                    food.setIsCustom(false);
                    storage.getFoods().put(foodInfo.getFoodName(), calculateFoodToAdd(food, foodInfo.getAmount()));
                }

            } else {

                if (storage.getCustomFoods().containsKey(foodInfo.getFoodName())) {
                    Food currentFood = storage.getCustomFoods().get(foodInfo.getFoodName());
                    combineFoods(currentFood, calculateFoodToAdd(food, foodInfo.getAmount()));
                } else {
                    food.setIsCustom(true);
                    storage.getCustomFoods().put(foodInfo.getFoodName(),
                            calculateFoodToAdd(food, foodInfo.getAmount()));
                }

            }

            setStorageConsumedCalories(storage);

            storageRepository.save(storage);

        } catch (Exception e) {
            throw new FoodException("Food with name: " + foodInfo.getFoodName() + " not found.");
        }

    }

    public void changeFood(Long storageId, Long recordId, FoodUpdate foodInfo, String userToken, Boolean isCustom)
        throws StorageException, FoodException, InvalidJsonTokenException {

        Long userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));
        
        if (foodInfo.getFoodName() == null || foodInfo.getFoodName().isEmpty()) {
            throw new FoodException("Food name cannot be empty.");
        }
        if ( foodInfo.getAmount() != null && foodInfo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FoodException("Amount must be greater than 0.");
        }

        if (!isCustom) {
            if (storage.getFoods().containsKey(foodInfo.getFoodName())) {

                Food currentFood = storage.getFoods().get(foodInfo.getFoodName());

                Food newFood = changeFoodMeasurement(currentFood, foodInfo.getAmount());

                newFood.setSize(foodInfo.getAmount());

                storage.getFoods().put(foodInfo.getFoodName(), newFood);

            } else {
                throw new FoodException("Food with name: " + foodInfo.getFoodName() + " not found.");
            }
        } else {
            if (storage.getCustomFoods().containsKey(foodInfo.getFoodName())) {

                Food currentFood = storage.getCustomFoods().get(foodInfo.getFoodName());

                Food newFood = changeFoodMeasurement(currentFood, foodInfo.getAmount());

                newFood.setSize(foodInfo.getAmount());

                storage.getCustomFoods().put(foodInfo.getFoodName(), newFood);

            } else {
                throw new FoodException("Custom food with name: " + foodInfo.getFoodName() + " not found.");
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

    private Food changeFoodMeasurement(Food food, BigDecimal amount) {

        if (food.getSize().compareTo(amount) == 0) {
            return food;
        } else if (food.getSize().compareTo(amount) > 0) {

            BigDecimal multiplayer = food.getSize().divide(amount, 2, RoundingMode.HALF_UP);

            Food baseFood = new Food();
            baseFood.setName(food.getName());
            baseFood.setSize(amount);
            baseFood.setIsCustom(food.getIsCustom());

            baseFood.setCalories(food.getCalories().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setA(food.getA().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setD(food.getD().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setE(food.getE().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setK(food.getK().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setC(food.getC().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB1(food.getB1().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB2(food.getB2().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB3(food.getB3().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB5(food.getB5().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB6(food.getB6().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB7(food.getB7().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB9(food.getB9().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setB12(food.getB12().divide(multiplayer, 2, RoundingMode.HALF_UP));

            baseFood.setCalcium(food.getCalcium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setPhosphorus(food.getPhosphorus().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setMagnesium(food.getMagnesium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSodium(food.getSodium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setPotassium(food.getPotassium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setChloride(food.getChloride().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setIron(food.getIron().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setZinc(food.getZinc().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setCopper(food.getCopper().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setManganese(food.getManganese().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setIodine(food.getIodine().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSelenium(food.getSelenium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setFluoride(food.getFluoride().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setChromium(food.getChromium().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setMolybdenum(food.getMolybdenum().divide(multiplayer, 2, RoundingMode.HALF_UP));

            baseFood.setCarbohydrates(food.getCarbohydrates().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setProtein(food.getProtein().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setFat(food.getFat().divide(multiplayer, 2, RoundingMode.HALF_UP));

            baseFood.setFiber(food.getFiber().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setTransFat(food.getTransFat().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSaturatedFat(food.getSaturatedFat().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setSugar(food.getSugar().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setPolyunsaturatedFat(food.getPolyunsaturatedFat().divide(multiplayer, 2, RoundingMode.HALF_UP));
            baseFood.setMonounsaturatedFat(food.getMonounsaturatedFat().divide(multiplayer, 2, RoundingMode.HALF_UP));

            return baseFood;
        } else {
            BigDecimal multiplayer = amount.divide(food.getSize(), 2, RoundingMode.HALF_UP);
            Food baseFood = new Food();
            baseFood.setName(food.getName());
            baseFood.setSize(amount);
            baseFood.setIsCustom(food.getIsCustom());

            baseFood.setCalories(food.getCalories().multiply(multiplayer));
            baseFood.setA(food.getA().multiply(multiplayer));
            baseFood.setD(food.getD().multiply(multiplayer));
            baseFood.setE(food.getE().multiply(multiplayer));
            baseFood.setK(food.getK().multiply(multiplayer));
            baseFood.setC(food.getC().multiply(multiplayer));
            baseFood.setB1(food.getB1().multiply(multiplayer));
            baseFood.setB2(food.getB2().multiply(multiplayer));
            baseFood.setB3(food.getB3().multiply(multiplayer));
            baseFood.setB5(food.getB5().multiply(multiplayer));
            baseFood.setB6(food.getB6().multiply(multiplayer));
            baseFood.setB7(food.getB7().multiply(multiplayer));
            baseFood.setB9(food.getB9().multiply(multiplayer));
            baseFood.setB12(food.getB12().multiply(multiplayer));

            baseFood.setCalcium(food.getCalcium().multiply(multiplayer));
            baseFood.setPhosphorus(food.getPhosphorus().multiply(multiplayer));
            baseFood.setMagnesium(food.getMagnesium().multiply(multiplayer));
            baseFood.setSodium(food.getSodium().multiply(multiplayer));
            baseFood.setPotassium(food.getPotassium().multiply(multiplayer));
            baseFood.setChloride(food.getChloride().multiply(multiplayer));
            baseFood.setIron(food.getIron().multiply(multiplayer));
            baseFood.setZinc(food.getZinc().multiply(multiplayer));
            baseFood.setCopper(food.getCopper().multiply(multiplayer));
            baseFood.setManganese(food.getManganese().multiply(multiplayer));
            baseFood.setIodine(food.getIodine().multiply(multiplayer));
            baseFood.setSelenium(food.getSelenium().multiply(multiplayer));
            baseFood.setFluoride(food.getFluoride().multiply(multiplayer));
            baseFood.setChromium(food.getChromium().multiply(multiplayer));
            baseFood.setMolybdenum(food.getMolybdenum().multiply(multiplayer));

            baseFood.setCarbohydrates(food.getCarbohydrates().multiply(multiplayer));
            baseFood.setProtein(food.getProtein().multiply(multiplayer));
            baseFood.setFat(food.getFat().multiply(multiplayer));

            baseFood.setFiber(food.getFiber().multiply(multiplayer));
            baseFood.setTransFat(food.getTransFat().multiply(multiplayer));
            baseFood.setSaturatedFat(food.getSaturatedFat().multiply(multiplayer));
            baseFood.setSugar(food.getSugar().multiply(multiplayer));
            baseFood.setPolyunsaturatedFat(food.getPolyunsaturatedFat().multiply(multiplayer));
            baseFood.setMonounsaturatedFat(food.getMonounsaturatedFat().multiply(multiplayer));

            return baseFood;
        }
    }

    private Food calculateFoodToAdd(Food food, BigDecimal amount) {

        BigDecimal multiplier = amount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        Food foodToAdd = new Food();
        foodToAdd.setIsCustom(food.getIsCustom());
        foodToAdd.setName(food.getName());
        foodToAdd.setSize(amount);
        foodToAdd.setCalories(food.getCalories().multiply(multiplier));

        foodToAdd.setA(food.getA().multiply(multiplier));
        foodToAdd.setD(food.getD().multiply(multiplier));
        foodToAdd.setE(food.getE().multiply(multiplier));
        foodToAdd.setK(food.getK().multiply(multiplier));
        foodToAdd.setC(food.getC().multiply(multiplier));
        foodToAdd.setB1(food.getB1().multiply(multiplier));
        foodToAdd.setB2(food.getB2().multiply(multiplier));
        foodToAdd.setB3(food.getB3().multiply(multiplier));
        foodToAdd.setB5(food.getB5().multiply(multiplier));
        foodToAdd.setB6(food.getB6().multiply(multiplier));
        foodToAdd.setB7(food.getB7().multiply(multiplier));
        foodToAdd.setB9(food.getB9().multiply(multiplier));
        foodToAdd.setB12(food.getB12().multiply(multiplier));

        foodToAdd.setCalcium(food.getCalcium().multiply(multiplier));
        foodToAdd.setPhosphorus(food.getPhosphorus().multiply(multiplier));
        foodToAdd.setMagnesium(food.getMagnesium().multiply(multiplier));
        foodToAdd.setSodium(food.getSodium().multiply(multiplier));
        foodToAdd.setPotassium(food.getPotassium().multiply(multiplier));
        foodToAdd.setChloride(food.getChloride().multiply(multiplier));
        foodToAdd.setIron(food.getIron().multiply(multiplier));
        foodToAdd.setZinc(food.getZinc().multiply(multiplier));
        foodToAdd.setCopper(food.getCopper().multiply(multiplier));
        foodToAdd.setManganese(food.getManganese().multiply(multiplier));
        foodToAdd.setIodine(food.getIodine().multiply(multiplier));
        foodToAdd.setSelenium(food.getSelenium().multiply(multiplier));
        foodToAdd.setFluoride(food.getFluoride().multiply(multiplier));
        foodToAdd.setChromium(food.getChromium().multiply(multiplier));
        foodToAdd.setMolybdenum(food.getMolybdenum().multiply(multiplier));

        foodToAdd.setCarbohydrates(food.getCarbohydrates().multiply(multiplier));
        foodToAdd.setProtein(food.getProtein().multiply(multiplier));
        foodToAdd.setFat(food.getFat().multiply(multiplier));
        foodToAdd.setFiber(food.getFiber().multiply(multiplier));
        foodToAdd.setTransFat(food.getTransFat().multiply(multiplier));
        foodToAdd.setSaturatedFat(food.getSaturatedFat().multiply(multiplier));
        foodToAdd.setSugar(food.getSugar().multiply(multiplier));
        foodToAdd.setPolyunsaturatedFat(food.getPolyunsaturatedFat().multiply(multiplier));
        foodToAdd.setMonounsaturatedFat(food.getMonounsaturatedFat().multiply(multiplier));

        return foodToAdd;
    }

    private Food combineFoods(Food food, Food foodToCombine) {

        food.setSize(food.getSize().add(foodToCombine.getSize()));
        food.setCalories(food.getCalories().add(foodToCombine.getCalories()));
        food.setIsCustom(foodToCombine.getIsCustom());

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
}
