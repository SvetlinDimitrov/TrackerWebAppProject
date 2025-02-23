package org.record.features.food.service;

import static org.record.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.shared.FoodRequest;
import org.example.domain.food.shared.OwnedFoodView;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.UserExtractor;
import org.record.features.food.dto.FoodFilter;
import org.record.features.food.entity.Food;
import org.record.features.food.repository.FoodRepository;
import org.record.features.food.repository.FoodSpecification;
import org.record.features.meal.services.MealService;
import org.record.infrastructure.mappers.FoodMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FoodServiceImpl implements FoodService {

  private final MealService mealService;
  private final FoodMapper foodMapper;
  private final FoodRepository repository;

  public Page<OwnedFoodView> getAll(UUID mealId, FoodFilter filter, Pageable pageable,
      String userToken) {
    var user = UserExtractor.get(userToken);

    FoodSpecification specification = new FoodSpecification(mealId, user.id(), filter);

    return repository.findAll(specification, pageable).map(foodMapper::toView);
  }

  public OwnedFoodView get(UUID foodId, UUID mealId, String userToken) {
    var user = UserExtractor.get(userToken);
    return foodMapper.toView(findByIdMealIdAndUserId(foodId, mealId, user.id()));
  }

  public OwnedFoodView create(UUID mealId, FoodRequest dto, String userToken) {
    var userId = UserExtractor.get(userToken).id();
    var meal = mealService.findByIdAndUserId(mealId, userId);

    var food = foodMapper.toEntity(dto);
    food.setMeal(meal);
    food.setUserId(userId);

    return foodMapper.toView(repository.save(food));
  }

  @Transactional
  public OwnedFoodView update(UUID mealId, UUID foodId, FoodRequest dto, String userToken) {
    var userId = UserExtractor.get(userToken).id();
    var food = findByIdMealIdAndUserId(foodId, mealId, userId);

    Food entity = foodMapper.toEntity(dto);
    entity.setMeal(food.getMeal());
    entity.setUserId(userId);

    repository.delete(food);
    return foodMapper.toView(repository.save(entity));
  }

  public void delete(UUID mealId, UUID foodId, String userToken) {
    var userId = UserExtractor.get(userToken).id();

    if (!repository.existsByIdAndMealIdAndUserId(foodId, mealId, userId)) {
      throw new NotFoundException(FOOD_NOT_FOUND, foodId);
    }

    repository.deleteById(foodId);
  }

  public Food findByIdMealIdAndUserId(UUID foodId, UUID mealId, UUID userId) {
    return repository.findByIdAndMealIdAndUserId(foodId, mealId, userId)
        .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND, foodId));
  }
}