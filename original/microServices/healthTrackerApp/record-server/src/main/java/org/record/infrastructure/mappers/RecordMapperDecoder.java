package org.record.infrastructure.mappers;

import java.util.List;
import java.util.Map;
import org.example.domain.record.dtos.NutritionIntakeView;
import org.example.domain.record.dtos.RecordView;
import org.example.domain.storage.dto.StorageView;
import org.example.domain.user.dto.UserView;
import org.record.features.record.entity.Record;
import org.record.features.record.utils.NutrientIntakeCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class RecordMapperDecoder implements RecordMapper {

  private RecordMapper delegate;
  private NutrientIntakeCreator nutrientIntakeCreator;

  @Override
  public RecordView toView(Record record, UserView user) {
    var recordView = delegate.toView(record, user);

    Map<String, NutritionIntakeView> nutationsMap = nutrientIntakeCreator.create(
        user.gender(),
        record.getDailyCalories(),
        user.workoutState());

    calculateNutrientsAndDailyConsumedCalories(recordView, nutationsMap);

    recordView.setNutritionIntakesViews(nutationsMap.values().stream().toList());

    return recordView;
  }

  @Autowired
  public void setDelegate(@Qualifier("delegate") RecordMapper delegate) {
    this.delegate = delegate;
  }

  @Autowired
  public void setNutrientIntakeCreator(NutrientIntakeCreator nutrientIntakeCreator) {
    this.nutrientIntakeCreator = nutrientIntakeCreator;
  }

  private void calculateNutrientsAndDailyConsumedCalories(
      RecordView record,
      Map<String, NutritionIntakeView> nutationsMap) {

    for (StorageView storage : record.getStorageViews()) {

      for (FoodView food : storage.getFoods()) {
        fillNutrientsMap(nutationsMap, food.getMineralNutrients());
        fillNutrientsMap(nutationsMap, food.getVitaminNutrients());
        fillNutrientsMap(nutationsMap, food.getMacroNutrients());
      }
      if (storage.getConsumedCalories() != null) {
        record.setDailyConsumedCalories(
            record.getDailyConsumedCalories().add(storage.getConsumedCalories()));
      }
    }
  }

  private void fillNutrientsMap(Map<String, NutritionIntakeView> nutationsMap,
      List<NutrientView> macronutrients) {
    for (NutrientView nutrient : macronutrients) {
      NutritionIntakeView nutritionIntake = nutationsMap.get(nutrient.name());
      nutritionIntake.setDailyConsumed(
          nutritionIntake.getDailyConsumed().add(nutrient.amount()));
    }
  }
}
