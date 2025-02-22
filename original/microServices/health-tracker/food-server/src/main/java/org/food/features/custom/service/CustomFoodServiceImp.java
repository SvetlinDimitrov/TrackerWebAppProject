package org.food.features.custom.service;

import static org.food.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.shared.FoodCreateRequest;
import org.example.domain.food.shared.FoodView;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.UserExtractor;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.food.features.custom.repository.CustomFoodRepository;
import org.food.features.custom.repository.specifications.CustomFoodSpecification;
import org.food.infrastructure.mappers.FoodMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomFoodServiceImp implements CustomFoodService {

  private final CustomFoodRepository repository;
  private final FoodMapper foodMapper;

  public Page<FoodView> getAll(String userToken, Pageable pageable,
      CustomFilterCriteria filterCriteria) {
    UUID userId = UserExtractor.get(userToken).id();

    return repository.findAll(new CustomFoodSpecification(userId, filterCriteria), pageable)
        .map(foodMapper::toView);
  }

  public FoodView getById(UUID id, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    return repository
        .findById(id)
        .filter(customFood -> customFood.getUserId().equals(userId))
        .map(foodMapper::toView)
        .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND, id));
  }

  public FoodView create(FoodCreateRequest dto, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    var entity = foodMapper.toEntity(dto);

    entity.setUserId(userId);

    return foodMapper.toView(repository.save(entity));
  }

  public void delete(UUID id, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    repository
        .findById(id)
        .filter(customFood -> customFood.getUserId().equals(userId))
        .ifPresentOrElse(
            repository::delete,
            () -> {
              throw new NotFoundException(FOOD_NOT_FOUND, id);
            });
  }
}
