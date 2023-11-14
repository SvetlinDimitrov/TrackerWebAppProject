package org.storage.services;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageKafkaService {

    private final KafkaTemplate<String, FoodView> kafkaTemplateFood;
    private final FoodClient foodClient;
    private final StorageRepository storageRepository;

    @KafkaListener(topics = "record-creation", groupId = "group_three", containerFactory = "kafkaListenerCreation")
    public void recordFirstCreation(@Payload RecordCreation recordCreationDto) {
        storageRepository.saveAllAndFlush(
                List.of(new Storage("First Meal", recordCreationDto.getRecordId()),
                        new Storage("Second Meal", recordCreationDto.getRecordId()),
                        new Storage("Third Meal", recordCreationDto.getRecordId()),
                        new Storage("Snacks", recordCreationDto.getRecordId())));
    }

    @KafkaListener(topics = "record-deletion", groupId = "group_four", containerFactory = "kafkaListenerDeletion")
    @Transactional
    public void recordDeletion(@Payload Long recordId) {
        storageRepository.deleteAllByRecordId(recordId);
    }

    @KafkaListener(topics = "storage-creation", groupId = "group_storage_single_creation_one", containerFactory = "kafkaListenerSingleCreation")
    public void createStorage(@Payload StorageCreation dto) {
        storageRepository.saveAndFlush(new Storage(dto.getStorageName(), dto.getRecordId()));
    }

    @Transactional
    @KafkaListener(topics = "storage-deletion", groupId = "group_storage_single_deletion_one", containerFactory = "kafkaListenerSingleDeletion")
    public void deleteStorage(@Payload StorageDeletion dto) {
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

        Message<FoodView> message = MessageBuilder
                .withPayload(foodByName)
                .setHeader(KafkaHeaders.TOPIC, "storage-removing")
                .build();

        kafkaTemplateFood
                .send(message);
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

            Message<FoodView> message = MessageBuilder
                    .withPayload(foodByName)
                    .setHeader(KafkaHeaders.TOPIC, "storage-filling")
                    .build();

            kafkaTemplateFood
                    .send(message);

            storageRepository.save(entity);
        } catch (RuntimeException e) {
            throw (FoodNotFoundException) e.getCause();
        }

    }
}