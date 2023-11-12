package org.nutrition.services;

import java.util.List;

import org.nutrition.NutritionIntakeRepository;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.FoodView;
import org.nutrition.model.dtos.RecordCreation;
import org.nutrition.model.entity.NutritionIntake;
import org.nutrition.utils.NutrientIntakeCreator;
import org.nutrition.utils.NutritionIntakeChanger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NutritionIntakeKafkaService {

    private final NutrientIntakeCreator nutrientIntakeCreator;
    private final NutritionIntakeRepository repository;

    @KafkaListener(topics = "record-creation", groupId = "group_one", containerFactory = "kafkaListenerCreation")
    public void create(@Payload RecordCreation createDto) {

        nutrientIntakeCreator.create(createDto)
                .forEach(entity -> repository.save(entity));
    }

    @KafkaListener(topics = "record-deletion", groupId = "group_two", containerFactory = "kafkaListenerDeletion")
    @Transactional
    public void deleteNutritionIntakesByRecordId(@Payload Long recordId) {
        repository.deleteAllByRecordId(recordId);
    }

    @KafkaListener(topics = "storage-filling", groupId = "group_storage_filling_removing_1", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeAdding(FoodView foodView) throws RecordNotFoundException {

        List<NutritionIntake> nutritionIntake = repository
                .findAllByRecordId(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "No nutritionIntakes was found with id:" + foodView.getRecordId()));

        nutritionIntake.forEach(entity -> NutritionIntakeChanger.fillNutritionChanges(entity, foodView));
        repository.saveAll(nutritionIntake);

    }

    @KafkaListener(topics = "storage-removing", groupId = "group_storage_filling_removing_1", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeRemoving(FoodView foodView) throws RecordNotFoundException {

        List<NutritionIntake> nutritionIntake = repository
                .findAllByRecordId(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "No nutritionIntakes was found with id:" + foodView.getRecordId()));

        nutritionIntake.forEach(entity -> NutritionIntakeChanger.removeNutritionChange(entity, foodView));
        repository.saveAll(nutritionIntake);

    }

}
