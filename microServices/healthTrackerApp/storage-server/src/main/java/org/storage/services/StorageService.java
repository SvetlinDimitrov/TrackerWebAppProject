package org.storage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.storage.StorageRepository;
import org.storage.client.User;
import org.storage.exception.FoodException;
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.dto.FoodInsertDto;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Food;
import org.storage.model.entity.Nutrient;
import org.storage.model.entity.Storage;
import org.storage.utils.GsonWrapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final GsonWrapper gsonWrapper;

    public StorageView getStorageByIdAndRecordId(String storageId, String recordId, String userToken)
            throws StorageException, InvalidJsonTokenException {
        String userId = getUserId(userToken);

        return toStorageView(
                storageRepository
                        .findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                        .orElseThrow(() -> new StorageException(
                                "Storage with ID: " + storageId + " not found with record id: " + recordId)));

    }

    public List<StorageView> getAllByRecordId(String recordId, String userToken) throws InvalidJsonTokenException {

        String userId = getUserId(userToken);

        List<Storage> storages = storageRepository.findAllByRecordIdAndUserId(recordId, userId);

        if (storages.isEmpty()) {
            return new ArrayList<>();
        }

        return storages
                .stream()
                .map(this::toStorageView)
                .collect(Collectors.toList());

    }

    public void createStorage(String recordId, String storageName, String userToken) throws InvalidJsonTokenException {
        String userId = getUserId(userToken);

        Storage storage = new Storage();
        storage.setUserId(userId);
        storage.setRecordId(recordId);
        storage.setConsumedCalories(BigDecimal.ZERO);
        storage.setCustomFoods(new HashMap<>());
        storage.setFoods(new HashMap<>());

        storage.setName(Objects.requireNonNullElseGet(storageName, () -> "Default" + generateRandomNumbers()));

        storageRepository.save(storage);
    }

    public void deleteStorage(String recordId, String storageId, String userToken)
            throws StorageException, InvalidJsonTokenException {

        String userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId));

        storageRepository.delete(storage);
    }

    public void deleteAllByRecordIdAndUserId(String recordId, String userToken) throws InvalidJsonTokenException {
        String userId = getUserId(userToken);

        storageRepository.deleteAllByRecordIdAndUserId(recordId, userId);
    }

    public void addFood(String storageId, String recordId, FoodInsertDto foodDto, String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException {
        Food food = foodDto.toFood();

        validateFood(food);
        String userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));

        if (!food.getFoodClass().equals("Custom")) {

            if (storage.getFoods().containsKey(food.getId())) {
                Food currentFood = storage.getFoods().get(food.getId());
                combineFoods(currentFood, food);
            } else {
                storage.getFoods().put(food.getId(), food);
            }

        } else {

            if (storage.getCustomFoods().containsKey(food.getId())) {
                Food currentFood = storage.getCustomFoods().get(food.getId());
                combineFoods(currentFood, food);
            } else {
                storage.getCustomFoods().put(food.getId(), food);
            }

        }

        setStorageConsumedCalories(storage);

        storageRepository.save(storage);

    }

    public void changeFood(String storageId, String recordId, FoodInsertDto foodDto, String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException {
        Food food = foodDto.toFood();

        validateFood(food);
        String userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));

        if (!food.getFoodClass().equals("Custom")) {
            if (storage.getFoods().containsKey(food.getId())) {
                storage.getFoods().put(food.getId(), food);
            } else {
                throw new FoodException("Food with name: " + food.getId() + " not found.");
            }
        } else {
            if (storage.getCustomFoods().containsKey(food.getId())) {
                storage.getCustomFoods().put(food.getId(), food);
            } else {
                throw new FoodException("Custom food with name: " + food.getId() + " not found.");
            }
        }

        setStorageConsumedCalories(storage);

        storageRepository.save(storage);

    }

    public void removeFood(String storageId, String recordId, String foodId, String userToken, Boolean isCustom)
            throws FoodException, StorageException, InvalidJsonTokenException {

        String userId = getUserId(userToken);

        Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
                .orElseThrow(() -> new StorageException(
                        "Storage with ID: " + storageId + " not found with record id: " + recordId + " and user id: "
                                + userId));

        if (!isCustom) {
            if (storage.getFoods().containsKey(foodId)) {
                storage.getFoods().remove(foodId);
            } else {
                throw new FoodException("Food with ID: " + foodId + " not found.");
            }
        } else {
            if (storage.getCustomFoods().containsKey(foodId)) {
                storage.getCustomFoods().remove(foodId);
            } else {
                throw new FoodException("Custom food with ID: " + foodId + " not found.");
            }
        }

        setStorageConsumedCalories(storage);

        storageRepository.save(storage);
    }

    private void combineFoods(Food food, Food foodToCombine) {

        food.setSize(food.getSize().add(foodToCombine.getSize()));
        food.getCalories().setAmount(food.getCalories().getAmount().add(foodToCombine.getCalories().getAmount()));

        fillNutrients(food.getVitaminNutrients(), foodToCombine.getVitaminNutrients());
        fillNutrients(food.getMineralNutrients(), foodToCombine.getMineralNutrients());
        fillNutrients(food.getMacronutrients(), foodToCombine.getMacronutrients());
    }

    private static void fillNutrients(List<Nutrient> real, List<Nutrient> toAdd) {
        for(Nutrient nutrient : toAdd) {
            boolean found = false;
            for(Nutrient nutrientToAdd : real) {
                if(nutrient.getName().equals(nutrientToAdd.getName())) {
                    nutrientToAdd.setAmount(nutrientToAdd.getAmount().add(nutrient.getAmount()));
                    found = true;
                    break;
                }
            }
            if(!found) {
                real.add(nutrient);
            }
        }
    }

    private String getUserId(String getUserId) throws InvalidJsonTokenException {
        try {
            return gsonWrapper.fromJson(getUserId, User.class).getId();
        } catch (Exception e) {
            throw new InvalidJsonTokenException("Invalid token.");
        }
    }

    private String generateRandomNumbers() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
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
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b.getCalories().getAmount()), BigDecimal::add);

        storage.setConsumedCalories(consumedCalories);
    }

    private void validateFood(Food food) throws FoodException {
        if (food == null) {
            throw new FoodException("Food is required.");
        }
        if (food.getDescription() == null || food.getDescription().isEmpty()) {
            throw new FoodException("Food name is required.");
        }
        if (food.getId() == null || food.getId().isEmpty()) {
            throw new FoodException("Food id is required.");
        }
        if (food.getMeasurement() == null || food.getMeasurement().isEmpty()) {
            throw new FoodException("Measurement is required.");
        }
        if (food.getSize() == null || food.getSize().compareTo(BigDecimal.ZERO) < 0) {
            throw new FoodException("Invalid size.");
        }
        validateFoodClass(food);
        validateCalories(food);
        validateMinerals(food.getMineralNutrients());
        validateVitamins(food.getVitaminNutrients());
        validateMacronutrients(food.getMacronutrients());
    }

    private void validateFoodClass(Food food) throws FoodException {
        Set<String> macronutrientNames = Set.of(
                "Custom",
                "Branded",
                "FinalFood",
                "Survey");
        if (food.getFoodClass() == null || food.getFoodClass().isEmpty() || !macronutrientNames.contains(food.getFoodClass())) {
            throw new FoodException("Invalid food class. Valid names: " + String.join(", ", macronutrientNames));
        }

    }

    private void validateCalories(Food food) throws FoodException {
        if (food.getCalories() == null ||
                food.getCalories().getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                food.getCalories().getUnit() == null || food.getCalories().getUnit().isEmpty() || !food.getCalories().getUnit().equals("kcal") ||
                food.getCalories().getName() == null || food.getCalories().getName().isEmpty() || !food.getCalories().getName().equals("Energy"))  {
            throw new FoodException("Invalid calories.");
        }
    }

    private void validateMacronutrients(List<Nutrient> macronutrients) throws FoodException {
        Set<String> macronutrientNames = Set.of(
                "Carbohydrates",
                "Protein",
                "Fat",
                "Fiber",
                "Trans Fat",
                "Saturated Fat",
                "Sugar",
                "Polyunsaturated Fat",
                "Monounsaturated Fat"
        );
        if (macronutrients.isEmpty()) {
            return;
        }
        for (Nutrient nutrient : macronutrients) {
            if (nutrient.getName() == null || nutrient.getName().isEmpty() ||
                    nutrient.getAmount() == null || nutrient.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                    nutrient.getUnit() == null || nutrient.getUnit().isEmpty()) {
                throw new FoodException("Invalid macronutrient.");
            }
            if (!macronutrientNames.contains(nutrient.getName())) {
                throw new FoodException("Invalid macronutrient. "+ nutrient.getName() + " Valid names: "+ String.join(", ", macronutrientNames));
            }
        }
    }

    private void validateVitamins(List<Nutrient> vitaminNutrients) throws FoodException {
        Set<String> vitaminNames = Set.of(
                "Vitamin A",
                "Vitamin D (D2 + D3)",
                "Vitamin E",
                "Vitamin K",
                "Vitamin C",
                "Vitamin B1 (Thiamin)",
                "Vitamin B2 (Riboflavin)",
                "Vitamin B3 (Niacin)",
                "Vitamin B5 (Pantothenic acid)",
                "Vitamin B6",
                "Vitamin B7 (Biotin)",
                "Vitamin B9 (Folate)",
                "Vitamin B12"
        );
        if (vitaminNutrients.isEmpty()) {
            return;
        }
        for (Nutrient nutrient : vitaminNutrients) {
            if (nutrient.getName() == null || nutrient.getName().isEmpty() ||
                    nutrient.getAmount() == null || nutrient.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                    nutrient.getUnit() == null || nutrient.getUnit().isEmpty()) {
                throw new FoodException("Invalid vitamin nutrient.");
            }
            if (!vitaminNames.contains(nutrient.getName())) {
                throw new FoodException("Invalid vitamin nutrient. "+ nutrient.getName() +
                        " Valid names: " + String.join(", ", vitaminNames));
            }
        }
    }

    private void validateMinerals(List<Nutrient> mineralNutrients) throws FoodException {
        Set<String> mineralNames = Set.of(
                "Calcium , Ca",
                "Phosphorus , P",
                "Magnesium , Mg",
                "Sodium , Na",
                "Potassium , K",
                "Iron , Fe",
                "Zinc , Zn",
                "Copper , Cu",
                "Manganese , Mn",
                "Iodine , I",
                "Selenium , Se",
                "Molybdenum , Mo"
        );
        if (mineralNutrients.isEmpty()) {
            return;
        }
        for (Nutrient nutrient : mineralNutrients) {
            if (nutrient.getName() == null || nutrient.getName().isEmpty() ||
                    nutrient.getAmount() == null || nutrient.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                    nutrient.getUnit() == null || nutrient.getUnit().isEmpty()) {
                throw new FoodException("Invalid mineral nutrient.");
            }
            if (!mineralNames.contains(nutrient.getName())) {
                throw new FoodException("Invalid mineral nutrient. " + nutrient.getName() +
                        " Valid names: " + String.join(", ", mineralNames));
            }
        }
    }

    public Food getFoodByStorage(String storageId, String recordId, String foodName, String userToken, Boolean isCustom)
            throws InvalidJsonTokenException, StorageException, FoodException {

        String userId = getUserId(userToken);

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
