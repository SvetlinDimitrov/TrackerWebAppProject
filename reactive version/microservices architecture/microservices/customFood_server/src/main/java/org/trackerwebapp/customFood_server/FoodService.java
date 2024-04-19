package org.trackerwebapp.customFood_server;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.trackerwebapp.customFood_server.domain.dto.CalorieView;
import org.trackerwebapp.customFood_server.domain.dto.FoodView;
import org.trackerwebapp.customFood_server.domain.dto.InsertFoodDto;
import org.trackerwebapp.customFood_server.domain.dto.NutritionView;
import org.trackerwebapp.customFood_server.domain.entity.CalorieEntity;
import org.trackerwebapp.customFood_server.domain.entity.FoodEntity;
import org.trackerwebapp.customFood_server.domain.entity.NutritionEntity;
import org.trackerwebapp.customFood_server.repository.CalorieRepository;
import org.trackerwebapp.customFood_server.repository.FoodRepository;
import org.trackerwebapp.customFood_server.repository.NutritionRepository;
import org.trackerwebapp.customFood_server.utils.CalorieModifier;
import org.trackerwebapp.customFood_server.utils.FoodModifier;
import org.trackerwebapp.customFood_server.utils.NutritionModifier;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FoodService {

  private final FoodRepository repository;
  private final NutritionRepository nutritionRepository;
  private final CalorieRepository calorieRepository;

  public Flux<FoodView> getAllFoods(String userId) {
    return repository.findByUserId(userId)
        .flatMap(this::toFoodView);
  }

  public Mono<FoodView> getById(String userId, String foodId) {
    return getFoodEntityMono(userId, foodId)
        .flatMap(this::toFoodView);
  }

  public Mono<Void> deleteFood(String userId, String foodId) {
    return getFoodEntityMono(userId, foodId)
        .flatMap(entity -> repository.deleteById(entity.getId()));
  }

  public Mono<Void> createFood(String userId, InsertFoodDto dto) {
    return createAndFillFoodEntity(dto, userId)
        .flatMap(foodEntity -> Mono.zip(
            Mono.just(foodEntity),
            createAndFillCalorieEntity(dto.calories(), foodEntity.getId()),
            createAndFillNutritions(dto.nutrients(), foodEntity.getId())
        ))
        .flatMap(data ->
            repository.save(data.getT1())
                .then(calorieRepository.save(data.getT2()))
                .thenMany(nutritionRepository.saveAll(data.getT3()))
                .then()
        );
  }

  public Mono<FoodView> changeFood(String userId, String foodId, InsertFoodDto dto) {
    return createAndFillFoodEntity(dto, userId)
        .flatMap(foodEntity -> Mono.zip(
            Mono.just(foodEntity),
            createAndFillCalorieEntity(dto.calories(), foodEntity.getId()),
            createAndFillNutritions(dto.nutrients(), foodEntity.getId())
        ))
        .flatMap(data ->
            repository.deleteById(foodId)
                .then(repository.save(data.getT1()))
                .then(calorieRepository.save(data.getT2()))
                .thenMany(nutritionRepository.saveAll(data.getT3()))
                .then(getById(userId, data.getT1().getId()))
        );
  }

  private Mono<FoodEntity> getFoodEntityMono(String userId, String foodId) {
    return repository.findByIdAndUserId(foodId, userId)
        .switchIfEmpty(
            Mono.error(new BadRequestException("No such food exists with id: " + foodId)));
  }

  private Mono<FoodView> toFoodView(FoodEntity entity) {
    return calorieRepository.findByFoodId(entity.getId())
        .map(CalorieView::toView)
        .zipWith(nutritionRepository
            .findAllByFoodId(entity.getId())
            .map(NutritionView::toView)
            .collectList())
        .map(data -> FoodView.toView(entity, data.getT1(), data.getT2()));
  }

  private Mono<CalorieEntity> createAndFillCalorieEntity(CalorieView dto, String foodId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("calorie cannot be null"));
    }
    return Mono.just(new CalorieEntity())
        .flatMap(entity -> {
          entity.setFoodId(foodId);
          return Mono.just(entity);
        })
        .flatMap(calorieEntity -> CalorieModifier.validateAndUpdateSize(calorieEntity, dto));
  }

  private Mono<List<NutritionEntity>> createAndFillNutritions(List<NutritionView> dtoList,
      String foodId) {
    if (dtoList == null) {
      return Mono.error(new BadRequestException("nutrients cannot be null"));
    }
    return Flux.fromIterable(dtoList)
        .flatMap(dto -> {
          NutritionEntity nutrition = new NutritionEntity();
          nutrition.setFoodId(foodId);
          return NutritionModifier.validateAndUpdateName(nutrition, dto)
              .flatMap(updatedNutrition ->
                  NutritionModifier.validateAndUpdateUnit(updatedNutrition, dto))
              .flatMap(updatedNutrition ->
                  NutritionModifier.validateAndUpdateAmount(updatedNutrition, dto))
              .thenReturn(nutrition);
        })
        .collectList();
  }

  private Mono<FoodEntity> createAndFillFoodEntity(InsertFoodDto dto, String userId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("food cannot be null"));
    }
    return Mono.just(new FoodEntity())
        .flatMap(foodEntity -> FoodModifier.validateAndUpdateMeasurement(foodEntity, dto))
        .flatMap(foodEntity -> FoodModifier.validateAndUpdateName(foodEntity, dto))
        .flatMap(foodEntity -> FoodModifier.validateAndUpdateSize(foodEntity, dto))
        .flatMap(foodEntity -> {
          foodEntity.setUserId(userId);
          return Mono.just(foodEntity);
        });
  }
}
