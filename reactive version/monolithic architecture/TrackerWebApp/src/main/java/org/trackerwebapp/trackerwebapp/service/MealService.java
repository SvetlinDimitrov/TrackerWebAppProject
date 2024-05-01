package org.trackerwebapp.trackerwebapp.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.CreateMeal;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.FoodView;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.MealView;
import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.MealEntity;
import org.trackerwebapp.trackerwebapp.repository.FoodRepository;
import org.trackerwebapp.trackerwebapp.repository.MealRepository;
import org.trackerwebapp.trackerwebapp.utils.meals.MealModifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MealService extends AbstractFoodService {

  private final MealRepository mealRepository;

  public MealService(FoodRepository repository, MealRepository mealRepository) {
    super(repository);
    this.mealRepository = mealRepository;
  }

  public Flux<MealView> getAllByUserId(String userId) {
    return mealRepository.findAllMealsByUserId(userId)
        .flatMap(this::fetchMealView);
  }

  public Mono<MealView> getByIdAndUserId(String mealId, String userId) {
    return getMealEntityMono(userId, mealId)
        .flatMap(this::fetchMealView);
  }

  public Mono<MealView> createMeal(String userId, CreateMeal dto) {
    return
        mealRepository.findUserById(userId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
            .flatMap(entity -> MealModifier.validateAndUpdateEntity(dto, userId))
            .flatMap(entity ->
                mealRepository.saveMeal(entity)
                    .flatMap(savedEntity -> getByIdAndUserId(
                            savedEntity.getId(),
                            savedEntity.getUserId()
                        )
                    )
            );
  }

  public Mono<MealView> modifyMeal(String userId, CreateMeal dto, String mealId) {
    return getMealEntityMono(userId, mealId)
        .flatMap(entity -> MealModifier.validateAndUpdateEntity(entity, dto))
        .flatMap(entity ->
            mealRepository.updateMealNameByIdAndUserId(
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
        .flatMap(entity -> mealRepository.deleteMealById(entity.getId()));
  }

  private Mono<MealEntity> getMealEntityMono(String userId, String mealId) {
    return mealRepository
        .findMealByIdAndUserId(mealId, userId)
        .switchIfEmpty(Mono.error(new BadRequestException(
            "No meal found with id: " + mealId)));
  }

  private Mono<MealView> fetchMealView(MealEntity entity) {
    return Mono.just(entity)
        .flatMap(mealEntity -> Mono.zip(
            Mono.just(mealEntity),
            fetchFoodViewsByMealId(mealEntity.getId()),
            mealRepository
                .findCalorieByMealId(mealEntity.getId())
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
    return repository.findAllFoodsByMealId(mealId)
        .flatMap(data -> toFoodView(data , mealId))
        .collectList();
  }
}
