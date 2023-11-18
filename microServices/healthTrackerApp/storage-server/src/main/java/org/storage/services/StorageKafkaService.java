package org.storage.services;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.storage.StorageRepository;
import org.storage.client.FoodClient;
import org.storage.client.FoodView;
import org.storage.exception.FoodNameNotFoundException;
import org.storage.exception.FoodNotFoundException;
import org.storage.exception.StorageNotFoundException;
import org.storage.model.dto.RecordCreation;
import org.storage.model.dto.StorageCreation;
import org.storage.model.dto.StorageDeletion;
import org.storage.model.dto.StorageUpdate;
import org.storage.model.entity.Storage;
import org.storage.model.enums.KafkaProducerTopics;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplateFood;
    private final FoodClient foodClient;
    private final StorageRepository storageRepository;
    private Gson gson = new Gson();

 
    @KafkaListener(topics = "RECORD_DELETION", groupId = "group_four", containerFactory = "kafkaListenerDeletion")
    @Transactional
    public void recordDeletion(String jsonToken) {

        Long recordId = gson.fromJson(jsonToken, Long.class);

        storageRepository.deleteAllByRecordId(recordId);
    }

    @KafkaListener(topics = "STORAGE_CREATION", groupId = "group_storage_single_creation_one", containerFactory = "kafkaListenerSingleCreation")
    public void createStorage(String jsonToken) {

        StorageCreation dto = gson.fromJson(jsonToken, StorageCreation.class);

        storageRepository.saveAndFlush(new Storage(dto.getStorageName(), dto.getRecordId()));
    }

    @Transactional
    @KafkaListener(topics = "STORAGE_DELETION", groupId = "group_storage_single_deletion_one", containerFactory = "kafkaListenerSingleDeletion")
    public void deleteStorage(String jsonToken) {

        StorageDeletion dto = gson.fromJson(jsonToken, StorageDeletion.class);

        storageRepository.deleteByIdAndRecordId(dto.getStorageId(), dto.getRecordId());
    }

    @Transactional
    public void removeFoodFromStorage(StorageUpdate dto)
            throws StorageNotFoundException, FoodNameNotFoundException {
        Storage entity = storageRepository.findByIdAndRecordId(dto.getStorageId(), dto.getRecordId())
                .orElseThrow(() -> new StorageNotFoundException("Storage with the given id:"
                        + dto.getStorageId()
                        + " and record id:" + dto.getRecordId() + " does not exists"));

        if (!entity.getFoodNames().contains(dto.getFoodName())) {
            throw new FoodNameNotFoundException("Food with the given name:" + dto.getFoodName()
                    + " does not exists in storage with id:" + dto.getStorageId()
                    + " and record id:"
                    + dto.getRecordId());
        }

        entity.getFoodNames()
                .remove(dto.getFoodName());

        storageRepository.save(entity);

        FoodView foodByName = foodClient.getFoodByName(dto.getFoodName());
        foodByName.setRecordId(dto.getRecordId());

        String jsonToken = gson.toJson(foodByName);

        kafkaTemplateFood.send(KafkaProducerTopics.STORAGE_REMOVING.name(), jsonToken);
    }

    public void addFood(StorageUpdate dto) throws StorageNotFoundException, FoodNotFoundException {
        Storage entity = storageRepository.findByIdAndRecordId(dto.getStorageId(), dto.getRecordId())
                .orElseThrow(() -> new StorageNotFoundException("Storage with the given id:"
                        + dto.getStorageId()
                        + " and record id:" + dto.getRecordId() + " does not exists"));

        try {
            FoodView foodByName = foodClient.getFoodByName(dto.getFoodName());

            entity.getFoodNames()
                    .add(foodByName.getName());

            foodByName.setRecordId(dto.getRecordId());

            String jsonToken = gson.toJson(foodByName);

            kafkaTemplateFood
                    .send(KafkaProducerTopics.STORAGE_FILLING.name(), jsonToken);

            storageRepository.save(entity);
        } catch (RuntimeException e) {
            throw (FoodNotFoundException) e.getCause();
        }

    }
}