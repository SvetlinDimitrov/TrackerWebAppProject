package org.record.infrastructure.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.domain.food.shared.NutritionIntakeView;
import org.record.features.record.dto.RecordView;
import org.example.domain.user.dto.UserView;
import org.record.features.food.entity.Food;
import org.record.features.food.entity.Nutrient;
import org.record.features.record.entity.Record;
import org.record.features.record.utils.MacronutrientCreator;
import org.record.features.record.utils.MineralCreator;
import org.record.features.record.utils.VitaminCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class RecordMapperDecoder implements RecordMapper {

  private RecordMapper delegate;

  @Override
  public RecordView toView(Record record, UserView user) {
    var recordView = delegate.toView(record, user);

    double consumedCalories = 0;

    Map<String, NutritionIntakeView> nutritionMap = new HashMap<>();
    setVitaminIntakes(recordView, nutritionMap);
    setMineralsIntakes(recordView, nutritionMap);
    setMacrosIntakes(recordView, nutritionMap);

    List<Food> list = record.getMeals()
        .stream()
        .flatMap(meal -> meal.getFoods().stream())
        .toList();

    for (Food food : list) {
      fillNutrientsMap(nutritionMap, food.getNutrients());
      consumedCalories += food.getCalories().getAmount();
    }

    recordView.setConsumedCalories(consumedCalories);

    return recordView;
  }

  @Autowired
  public void setDelegate(@Qualifier("delegate") RecordMapper delegate) {
    this.delegate = delegate;
  }


  private void fillNutrientsMap(Map<String, NutritionIntakeView> nutationsMap,
      List<Nutrient> macronutrients) {
    for (Nutrient nutrient : macronutrients) {
      NutritionIntakeView nutritionIntake = nutationsMap.get(nutrient.getName());
      nutritionIntake.setDailyConsumed(
          nutritionIntake.getDailyConsumed() + nutrient.getAmount());
    }
  }

  private void setVitaminIntakes(RecordView view, Map<String, NutritionIntakeView> intakeViewMap) {
    view.setVitaminIntake(
        intakeViewMap.values()
            .stream()
            .filter(nutritionIntakeView -> VitaminCreator.allAllowedVitamins.contains(
                nutritionIntakeView.getName()))
            .toList());
  }

  private void setMineralsIntakes(RecordView view, Map<String, NutritionIntakeView> intakeViewMap) {
    view.setMineralIntakes(
        intakeViewMap.values()
            .stream()
            .filter(nutritionIntakeView -> MineralCreator.allAllowedMinerals.contains(
                nutritionIntakeView.getName()))
            .toList());
  }

  private void setMacrosIntakes(RecordView view, Map<String, NutritionIntakeView> intakeViewMap) {
    view.setMacroIntakes(
        intakeViewMap.values()
            .stream()
            .filter(nutritionIntakeView -> MacronutrientCreator.allAllowedMacros.contains(
                nutritionIntakeView.getName()))
            .toList());
  }
}
