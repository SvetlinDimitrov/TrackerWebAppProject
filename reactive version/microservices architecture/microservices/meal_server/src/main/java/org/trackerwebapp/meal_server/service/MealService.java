package org.trackerwebapp.meal_server.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.meal_server.domain.dto.CalorieView;
import org.trackerwebapp.meal_server.domain.dto.CreateMeal;
import org.trackerwebapp.meal_server.domain.dto.FoodView;
import org.trackerwebapp.meal_server.domain.dto.MealView;
import org.trackerwebapp.meal_server.domain.dto.NutritionView;
import org.trackerwebapp.meal_server.domain.entity.CalorieEntity;
import org.trackerwebapp.meal_server.domain.entity.MealEntity;
import org.trackerwebapp.meal_server.repository.CalorieRepository;
import org.trackerwebapp.meal_server.repository.FoodRepository;
import org.trackerwebapp.meal_server.repository.MealRepository;
import org.trackerwebapp.meal_server.repository.NutritionRepository;
import org.trackerwebapp.meal_server.repository.UserRepository;
import org.trackerwebapp.meal_server.utils.MealModifier;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                    entity.getName(),
                    entity.getId(),
                    entity.getUserId())
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
