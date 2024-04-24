package org.trackerwebapp.trackerwebapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.*;
import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.MealEntity;
import org.trackerwebapp.trackerwebapp.repository.*;
import org.trackerwebapp.trackerwebapp.utils.meals.MealModifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

  private final MealRepository repository;
  private final FoodRepository foodRepository;
  private final NutritionRepository nutritionRepository;
  private final CalorieRepository calorieRepository;
  private final UserRepository userRepository;

  public Flux<MealView> getAllByUserId(String userId) {
    return repository.findAllByUserId(userId)
        .flatMap(this::fetchMealView);
  }

  public Mono<MealView> getByIdAndUserId(String mealId, String userId) {
    return getMealEntityMono(userId, mealId)
        .flatMap(this::fetchMealView);
  }

  public Mono<MealView> createMeal(String userId, CreateMeal dto) {
    return
        userRepository.findById(userId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
            .map(user -> {
              MealEntity entity = new MealEntity();
              entity.setUserId(user.getId());
              return entity;
            })
            .flatMap(entity -> MealModifier.updateName(entity, dto))
            .flatMap(entity ->
                repository.save(entity)
                    .flatMap(savedEntity -> getByIdAndUserId(
                            savedEntity.getId(),
                            savedEntity.getUserId()
                        )
                    )
            );
  }

  public Mono<MealView> modifyMeal(String userId, CreateMeal dto, String mealId) {
    return getMealEntityMono(userId, mealId)
        .flatMap(entity -> MealModifier.updateName(entity, dto))
        .flatMap(entity ->
            repository.updateMealNameByIdAndUserId(
                    entity.getId(),
                    entity.getUserId(),
                    entity)
                .then(
                    getByIdAndUserId(entity.getId(), userId)
                )
        );
  }

  public Mono<Void> deleteByIdAndUserId(String mealId, String userId) {
    return getMealEntityMono(userId, mealId)
        .flatMap(entity -> repository.deleteById(entity.getId()));
  }

  private Mono<MealEntity> getMealEntityMono(String userId, String mealId) {
    return repository
        .findByIdAndUserId(mealId, userId)
        .switchIfEmpty(Mono.error(new BadRequestException(
            "No meal found with id: " + mealId)));
  }

  private Mono<MealView> fetchMealView(MealEntity entity) {
    return Mono.just(entity)
        .flatMap(mealEntity -> Mono.zip(
            Mono.just(mealEntity),
            fetchFoodViewsByMealId(mealEntity.getId()),
            calorieRepository
                .findByMealId(mealEntity.getId())
                .collectList()
        ))
        .map(tuple -> MealView.toView(
                tuple.getT1(),
                tuple.getT2(),
                tuple.getT3()
                    .stream()
                    .map(CalorieEntity::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
            )
        );
  }

  private Mono<List<FoodView>> fetchFoodViewsByMealId(String mealId) {
    return foodRepository.findAllByMealId(mealId)
        .flatMap(foodEntity -> Mono.zip(
            Mono.just(foodEntity),
            nutritionRepository.findAllByFoodId(foodEntity.getId())
                .map(NutritionView::toView)
                .collectList(),
            calorieRepository.findByFoodId(foodEntity.getId())
                .map(CalorieView::toView)
        ))
        .map(tuple -> FoodView.toView(tuple.getT1(), tuple.getT2(), tuple.getT3()))
        .collectList();
  }
}
