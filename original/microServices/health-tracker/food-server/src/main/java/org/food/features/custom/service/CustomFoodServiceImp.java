package org.food.features.custom.service;

import static org.food.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.shared.FoodRequest;
import org.example.domain.food.shared.FoodView;
import org.example.domain.food.shared.OwnedFoodView;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.UserExtractor;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.food.features.custom.entity.CustomFood;
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

  public Page<OwnedFoodView> getAll(String userToken, Pageable pageable,
      CustomFilterCriteria filterCriteria) {
    UUID userId = UserExtractor.get(userToken).id();

    return repository.findAll(new CustomFoodSpecification(userId, filterCriteria), pageable)
        .map(foodMapper::toView);
  }

  public OwnedFoodView getById(UUID id, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    return foodMapper.toView(findByIdAndUserId(id, userId));
  }

  public OwnedFoodView create(FoodRequest dto, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    var entity = foodMapper.toEntity(dto);

    entity.setUserId(userId);

    return foodMapper.toView(repository.save(entity));
  }

  public OwnedFoodView update(UUID id, FoodRequest request, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    validateFoodExists(id, userId);

    var entity = foodMapper.toEntity(request);
    entity.setUserId(userId);

    repository.deleteById(id);

    return foodMapper.toView(repository.save(entity));
  }

  public void delete(UUID id, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    validateFoodExists(id, userId);

    repository.deleteById(id);
  }

  public CustomFood findByIdAndUserId(UUID id, UUID userId) {
    return repository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND, id));
  }

  private void validateFoodExists(UUID id, UUID userId) {
    if (!repository.existsByIdAndUserId(id, userId)) {
      throw new NotFoundException(FOOD_NOT_FOUND, id);
    }
  }
}
