package org.trackerwebapp.trackerwebapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.trackerwebapp.domain.dto.NutritionIntakeView;
import org.trackerwebapp.trackerwebapp.domain.dto.record.CreateRecord;
import org.trackerwebapp.trackerwebapp.domain.dto.record.DistributedMacros;
import org.trackerwebapp.trackerwebapp.domain.dto.record.RecordView;
import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.UserDetails;
import org.trackerwebapp.trackerwebapp.domain.enums.Goals;
import org.trackerwebapp.trackerwebapp.repository.CalorieRepository;
import org.trackerwebapp.trackerwebapp.repository.NutritionRepository;
import org.trackerwebapp.trackerwebapp.repository.UserDetailsRepository;
import org.trackerwebapp.trackerwebapp.utils.BMRCalc;
import org.trackerwebapp.trackerwebapp.utils.DailyCaloriesCalculator;
import org.trackerwebapp.trackerwebapp.utils.record.DistributedMacrosValidator;
import org.trackerwebapp.trackerwebapp.utils.record.MacronutrientCreator;
import org.trackerwebapp.trackerwebapp.utils.record.MineralCreator;
import org.trackerwebapp.trackerwebapp.utils.record.VitaminCreator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

  private final UserDetailsRepository detailsRepository;
  private final CalorieRepository calorieRepository;
  private final NutritionRepository nutritionRepository;

  public Mono<RecordView> viewRecord(CreateRecord dto, String userId) {
    return detailsRepository.findByUserId(userId)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
        .flatMap(details -> Mono.zip(
            Mono.just(details),
            fetchRecordViewData(details, dto.goal())
        ))
        .flatMap(data -> setNutritionViews(data.getT1(), data.getT2(), dto.distributedMacros()));
  }

  private Mono<RecordView> fetchRecordViewData(UserDetails details, Goals goal) {
    return Mono.just(details)
        .flatMap(userDetails -> {
          RecordView view = new RecordView();
          view.setDailyCaloriesToConsume(
              DailyCaloriesCalculator.getCaloriesPerDay(
                  BMRCalc.calculateBMR(
                      userDetails.getGender(),
                      userDetails.getKilograms(),
                      userDetails.getHeight(),
                      userDetails.getAge()
                  ),
                  userDetails.getWorkoutState(),
                  Optional.ofNullable(goal)
                      .orElse(Goals.MaintainWeight)
              ).setScale(0, RoundingMode.DOWN)
          );
          return Mono.zip(
              Mono.just(userDetails),
              Mono.just(view)
          );
        }).flatMap(data -> calorieRepository.findByUserId(data.getT1().getUserId())
            .collectList()
            .map(list -> list.stream()
                .map(CalorieEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(0, RoundingMode.DOWN)
            )
            .map(dailyConsumedCalories -> {
              data.getT2().setDailyCaloriesConsumed(dailyConsumedCalories);
              return data.getT2();
            })
        );

  }

  private Mono<RecordView> setNutritionViews(UserDetails details, RecordView view, DistributedMacros distributedMacros) {

    return DistributedMacrosValidator.validate(distributedMacros)
        .flatMap(validatedDistribution ->
            Mono.zip(
                Mono.just(view),
                Mono.just(new HashMap<String, NutritionIntakeView>())
                    .map(map -> {
                      MineralCreator.fillMinerals(map, details.getGender(), details.getAge());
                      VitaminCreator.fillVitamins(map, details.getGender(), details.getAge());
                      MacronutrientCreator.fillMacros(map, view.getDailyCaloriesToConsume(), details.getGender(), validatedDistribution, details.getAge());
                      return map;
                    }),
                nutritionRepository.findByUserId(details.getUserId())
                    .collectList()
            ))
        .map(data -> {
          Map<String, NutritionIntakeView> nutritions = data.getT2();
          data.getT3().forEach(entity -> {
            NutritionIntakeView intakeView = nutritions.get(entity.getName());
            intakeView.setDailyConsumed(intakeView.getDailyConsumed().add(entity.getAmount()).setScale(0, RoundingMode.DOWN));
          });
          setVitaminIntakes(data.getT1(), nutritions);
          setMineralsIntakes(data.getT1(), nutritions);
          setMacrosIntakes(data.getT1(), nutritions);
          return data.getT1();
        });
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
