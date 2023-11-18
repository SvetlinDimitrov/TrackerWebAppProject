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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NutritionIntakeKafkaService {

    private final NutritionIntakeRepository repository;
    private final Gson gson;

  
    @KafkaListener(topics = "RECORD_DELETION", groupId = "group_two", containerFactory = "kafkaListenerDeletion")
    @Transactional
    public void deleteNutritionIntakesByRecordId(String jsonToken) {

        Long recordId = gson.fromJson(jsonToken, Long.class);

        repository.deleteAllByRecordId(recordId);
    }

    @KafkaListener(topics = "STORAGE_FILLING", groupId = "group_storage_filling_removing_1", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeAdding(String jsonToken) throws RecordNotFoundException {

        FoodView foodView = gson.fromJson(jsonToken, FoodView.class);

        List<NutritionIntake> nutritionIntake = repository
                .findAllByRecordId(foodView.getRecordId()).get();

        if (nutritionIntake.isEmpty()) {
            throw new RecordNotFoundException(
                    "No nutritionIntakes was found with id:" + foodView.getRecordId());
        }
        nutritionIntake.forEach(entity -> NutritionIntakeChanger.fillNutritionChanges(entity, foodView));
        repository.saveAll(nutritionIntake);

    }

    @KafkaListener(topics = "STORAGE_REMOVING", groupId = "group_storage_filling_removing_1", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeRemoving(String jsonToken) throws RecordNotFoundException {

        FoodView foodView = gson.fromJson(jsonToken, FoodView.class);

        List<NutritionIntake> nutritionIntake = repository
                .findAllByRecordId(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "No nutritionIntakes was found with id:" + foodView.getRecordId()));

        nutritionIntake.forEach(entity -> NutritionIntakeChanger.removeNutritionChange(entity, foodView));
        repository.saveAll(nutritionIntake);

    }

}
