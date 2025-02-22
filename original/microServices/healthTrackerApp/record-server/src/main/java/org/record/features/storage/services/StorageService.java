package org.record.features.storage.services;

import static org.record.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND;
import static org.record.infrastructure.exception.ExceptionMessages.RECORD_NOT_FOUND;
import static org.record.infrastructure.exception.ExceptionMessages.STORAGE_NOT_FOUND;
import static org.record.infrastructure.exception.ExceptionMessages.STORAGE_NOT_FOUND_WITH_RECORD_AND_USER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.custom.entity.CustomFoodEntity;
import org.example.domain.storage.dto.StorageView;
import org.example.exceptions.throwable.BadRequestException;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.UserExtractor;
import org.record.features.record.entity.Record;
import org.record.features.record.repository.RecordRepository;
import org.record.features.storage.entity.Storage;
import org.record.features.storage.repository.StorageRepository;
import org.record.infrastructure.mappers.CustomFoodMapper;
import org.record.infrastructure.mappers.StorageMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StorageService {

  private final StorageRepository storageRepository;
  private final RecordRepository recordRepository;
  private final CustomFoodMapper customFoodMapper;
  private final StorageMapper storageMapper;

  public StorageView getById(String storageId, String userToken) {
    return storageMapper.toView(
        storageRepository
            .findByIdAndRecord_UserId(storageId, UserExtractor.get(userToken).id())
            .orElseThrow(() -> new NotFoundException(STORAGE_NOT_FOUND, storageId)));
  }

  public List<StorageView> getAllByRecordId(String recordId, String userToken) {

    List<Storage> storages = storageRepository.findAllByRecordIdAndRecord_UserId(recordId,
        UserExtractor.get(userToken).id());

    if (storages.isEmpty()) {
      return new ArrayList<>();
    }

    return storages
        .stream()
        .map(storageMapper::toView)
        .collect(Collectors.toList());

  }

  public void create(String recordId, String storageName, String userToken) {
    Record record = recordRepository.findByIdAndUserId(recordId, UserExtractor.get(userToken).id())
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

    Storage storage = storageRepository.findByIdAndRecord_UserId(storageId,
            UserExtractor.get(userToken).id())
        .orElseThrow(() -> new NotFoundException(STORAGE_NOT_FOUND, storageId));

    storageRepository.delete(storage);
  }

  public void addFood(String storageId, String recordId, CustomFoodRequestCreate foodDto,
      String userToken) {
    CustomFoodEntity entity = customFoodMapper.toEntity(foodDto);

    UUID userId = UserExtractor.get(userToken).id();

    Storage storage = storageRepository.findByIdAndRecordIdAndRecord_UserId(storageId, recordId,
            userId)
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

    UUID userId = UserExtractor.get(userToken).id();

    Storage storage = storageRepository.findByIdAndRecordIdAndRecord_UserId(storageId, recordId,
            userId)
        .orElseThrow(() -> new BadRequestException(STORAGE_NOT_FOUND_WITH_RECORD_AND_USER,
            storageId, recordId, userId));

    storage.getFoods().put(foodId, entity);

    setStorageConsumedCalories(storage);

    storageRepository.save(storage);

  }

  public void removeFood(String storageId, String recordId, String foodId, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    Storage storage = storageRepository.findByIdAndRecordIdAndRecord_UserId(storageId, recordId,
            userId)
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

    UUID userId = UserExtractor.get(userToken).id();

    Storage storage = storageRepository.findByIdAndRecordIdAndRecord_UserId(storageId, recordId,
            userId)
        .orElseThrow(() -> new NotFoundException(STORAGE_NOT_FOUND_WITH_RECORD_AND_USER,
            storageId, recordId, userId));

    CustomFoodEntity food = storage.getFoods().get(foodId);

    if (food == null) {
      throw new NotFoundException(FOOD_NOT_FOUND, foodId);
    }

    return customFoodMapper.toView(food);
  }
}
