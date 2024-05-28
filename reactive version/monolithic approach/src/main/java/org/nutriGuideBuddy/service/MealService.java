package org.nutriGuideBuddy.service;

import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.meal.*;
import org.nutriGuideBuddy.domain.entity.CalorieEntity;
import org.nutriGuideBuddy.domain.entity.MealEntity;
import org.nutriGuideBuddy.repository.FoodRepository;
import org.nutriGuideBuddy.repository.MealRepository;
import org.nutriGuideBuddy.utils.meals.MealModifier;
import org.nutriGuideBuddy.utils.user.UserHelperFinder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MealService extends AbstractFoodService {

  private final MealRepository mealRepository;

  public MealService(FoodRepository repository, UserHelperFinder userHelper, MealRepository mealRepository) {
    super(repository, userHelper);
    this.mealRepository = mealRepository;
  }

  public Mono<Page<MealView>> getAllByUserId(Pageable pageable) {
    return userHelper.getUserId()
        .flatMap(userId -> mealRepository.findAllMealsByUserId(userId, pageable))
        .flatMap(page -> Flux.fromIterable(page)
            .flatMap(this::fetchMealView)
            .collectList()
            .map(list -> new PageImpl<>(list, pageable, list.size()))
        );
  }

  public Mono<Page<MealShortView>> getAllByUserIdShorten(Pageable pageable) {
    return userHelper.getUserId()
        .flatMap(userId -> mealRepository.findAllMealsByUserId(userId, pageable))
        .flatMap(page -> Flux.fromIterable(page)
            .flatMap(this::fetchShortenMealView)
            .collectList()
            .map(list -> new PageImpl<>(list, pageable, list.size()))
        );
  }

  public Mono<MealView> getByIdAndUserId(String mealId) {
    return getMealEntityMono(mealId)
        .flatMap(this::fetchMealView);
  }

  public Mono<MealView> createMeal(CreateMeal dto) {
    return userHelper.getUserId()
        .flatMap(mealRepository::findUserById)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .flatMap(entity -> MealModifier.validateAndUpdateEntity(dto, entity.getId()))
        .flatMap(entity ->
            mealRepository.saveMeal(entity).flatMap(savedEntity -> getByIdAndUserId(savedEntity.getId())
            )
        );
  }

  public Mono<MealView> modifyMeal(CreateMeal dto, String mealId) {
    return getMealEntityMono(mealId)
        .flatMap(entity -> MealModifier.validateAndUpdateEntity(entity, dto))
        .flatMap(entity -> mealRepository.updateMealNameByIdAndUserId(
                    entity.getId(),
                    entity.getUserId(),
                    entity
                )
                .then(getByIdAndUserId(entity.getId()))
        );
  }

  public Mono<Void> deleteByIdAndUserId(String mealId) {
    return getMealEntityMono(mealId)
        .flatMap(entity -> mealRepository.deleteMealById(entity.getId()));
  }

  private Mono<MealEntity> getMealEntityMono(String mealId) {
    return userHelper.getUserId()
        .flatMap(userId -> mealRepository.findMealByIdAndUserId(mealId, userId))
        .switchIfEmpty(Mono.error(new BadRequestException("No meal found with id: " + mealId)));
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

  private Mono<MealShortView> fetchShortenMealView(MealEntity entity) {
    return Mono.just(entity)
        .flatMap(mealEntity -> Mono.zip(
            Mono.just(mealEntity),
            fetchFoodViewsByMealIdShorten(mealEntity.getId()),
            mealRepository
                .findCalorieByMealId(mealEntity.getId())
                .collectList()
        ))
        .map(tuple -> MealShortView.toView(
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
        .flatMap(data -> toFoodView(data, mealId))
        .collectList();
  }

  private Mono<List<ShortenFood>> fetchFoodViewsByMealIdShorten(String mealId) {
    return repository.findAllFoodsByMealId(mealId)
        .flatMap(data -> toShortenFoodView(data, mealId))
        .collectList();
  }
}
