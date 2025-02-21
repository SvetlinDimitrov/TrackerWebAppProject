package org.record.features.storage.services;

import static org.record.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND;
import static org.record.infrastructure.exception.ExceptionMessages.INVALID_USER_TOKEN;
import static org.record.infrastructure.exception.ExceptionMessages.RECORD_NOT_FOUND;
import static org.record.infrastructure.exception.ExceptionMessages.STORAGE_NOT_FOUND;
import static org.record.infrastructure.exception.ExceptionMessages.STORAGE_NOT_FOUND_WITH_RECORD_AND_USER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.custom.dto.CustomFoodRequestCreate;
import org.example.domain.food.custom.entity.CustomFoodEntity;
import org.example.domain.food.shared.dto.FoodView;
import org.example.domain.storage.dto.StorageView;
import org.record.features.record.entity.Record;
import org.record.features.record.repository.RecordRepository;
import org.record.features.storage.entity.Storage;
import org.example.domain.user.dto.UserView;
import org.example.exceptions.throwable.BadRequestException;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.GsonWrapper;
import org.record.infrastructure.mappers.CustomFoodMapper;
import org.record.infrastructure.mappers.StorageMapper;
import org.springframework.stereotype.Service;
import org.record.features.storage.repository.StorageRepository;

@RequiredArgsConstructor
@Service
public class StorageService {

  private final StorageRepository storageRepository;
  private final RecordRepository recordRepository;
  private final GsonWrapper gsonWrapper = new GsonWrapper();
  private final CustomFoodMapper customFoodMapper;
  private final StorageMapper storageMapper;

  public StorageView getById(String storageId, String userToken) {
    String userId = getUserId(userToken);

    return storageMapper.toView(
        storageRepository
            .findByIdAndUserId(storageId, userId)
            .orElseThrow(() -> new NotFoundException(STORAGE_NOT_FOUND, storageId)));
  }

  public List<StorageView> getAllByRecordId(String recordId, String userToken) {

    String userId = getUserId(userToken);

    List<Storage> storages = storageRepository.findAllByRecordIdAndUserId(recordId, userId);

    if (storages.isEmpty()) {
      return new ArrayList<>();
    }

    return storages
        .stream()
        .map(storageMapper::toView)
        .collect(Collectors.toList());

  }

  public void create(String recordId, String storageName, String userToken) {
    Record record = recordRepository.findByIdAndUserId(recordId, getUserId(userToken))
        .orElseThrow(() -> new NotFoundException(RECORD_NOT_FOUND, recordId));

    Storage storage = new Storage();
    storage.setRecord(record);
    storage.setConsumedCalories(BigDecimal.ZERO);
    storage.setFoods(new HashMap<>());

    storage.setName(
        Objects.requireNonNullElseGet(storageName, () -> "Default" + generateRandomNumbers()));

    storageRepository.save(storage);
  }

  public void delete(String storageId, String userToken) {

    String userId = getUserId(userToken);

    Storage storage = storageRepository.findByIdAndUserId(storageId, userId)
        .orElseThrow(() -> new NotFoundException(STORAGE_NOT_FOUND, storageId));

    storageRepository.delete(storage);
  }

  public void addFood(String storageId, String recordId, CustomFoodRequestCreate foodDto,
      String userToken) {
    CustomFoodEntity entity = customFoodMapper.toEntity(foodDto);

    String userId = getUserId(userToken);

    Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
        .orElseThrow(() -> new BadRequestException(STORAGE_NOT_FOUND_WITH_RECORD_AND_USER,
            storageId, recordId, userId));

    storage.getFoods().put(entity.getId(), entity);

    setStorageConsumedCalories(storage);

    storageRepository.save(storage);

  }

  public void changeFood(String storageId, String recordId, String foodId,
      CustomFoodRequestCreate foodDto,
      String userToken) {
    CustomFoodEntity entity = customFoodMapper.toEntity(foodDto);

    String userId = getUserId(userToken);

    Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
        .orElseThrow(() -> new BadRequestException(STORAGE_NOT_FOUND_WITH_RECORD_AND_USER,
            storageId, recordId, userId));

    storage.getFoods().put(foodId, entity);

    setStorageConsumedCalories(storage);

    storageRepository.save(storage);

  }

  public void removeFood(String storageId, String recordId, String foodId, String userToken) {

    String userId = getUserId(userToken);

    Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
        .orElseThrow(() -> new BadRequestException(STORAGE_NOT_FOUND_WITH_RECORD_AND_USER,
            storageId, recordId, userId));

    if (storage.getFoods().containsKey(foodId)) {
      storage.getFoods().remove(foodId);
    } else {
      throw new NotFoundException(FOOD_NOT_FOUND, foodId);
    }

    setStorageConsumedCalories(storage);

    storageRepository.save(storage);
  }

  private String getUserId(String getUserId) {
    try {
      return gsonWrapper.fromJson(getUserId, UserView.class).id();
    } catch (Exception e) {
      throw new BadRequestException(INVALID_USER_TOKEN);
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

  private void setStorageConsumedCalories(Storage storage) {
    BigDecimal consumedCalories = storage.getFoods()
        .values()
        .stream()
        .reduce(BigDecimal.ZERO, (a, b) -> a.add(b.getCalories().getAmount()), BigDecimal::add);

    storage.setConsumedCalories(consumedCalories);
  }

  public FoodView getFoodByStorage(String storageId, String recordId, String foodId,
      String userToken) {

    String userId = getUserId(userToken);

    Storage storage = storageRepository.findByIdAndRecordIdAndUserId(storageId, recordId, userId)
        .orElseThrow(() -> new NotFoundException(STORAGE_NOT_FOUND_WITH_RECORD_AND_USER,
            storageId, recordId, userId));

    CustomFoodEntity food = storage.getFoods().get(foodId);

    if (food == null) {
      throw new NotFoundException(FOOD_NOT_FOUND, foodId);
    }

    return customFoodMapper.toView(food);
  }
}
