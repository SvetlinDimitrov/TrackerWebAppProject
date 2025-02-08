package org.food.features.custom.service;

import static org.food.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND_WITH_ID;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.throwable.NotFoundException;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.food.features.custom.dto.CustomFoodRequestCreate;
import org.food.features.custom.dto.CustomFoodView;
import org.food.features.custom.entity.CustomFoodEntity;
import org.food.features.custom.repository.CustomFoodRepository;
import org.food.infrastructure.mappers.CustomFoodMapper;
import org.food.infrastructure.utils.FoodUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomFoodServiceImp implements CustomFoodService {

    private final CustomFoodRepository repository;
    private final CustomFoodMapper customFoodMapper;
    private final FoodUtils foodUtils;

    public Page<CustomFoodView> getAll(
        String userToken,
        Pageable pageable,
        CustomFilterCriteria filterCriteria
    ) {
        String userId = foodUtils.getUserId(userToken);
        return repository.findAllByUserId(userId, pageable ,filterCriteria);
    }

    public CustomFoodView getById(String id, String userToken) {
        String userId = foodUtils.getUserId(userToken);

        CustomFoodEntity food = repository
                .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND_WITH_ID, id));

        return customFoodMapper.toView(food);
    }

    public CustomFoodView create(CustomFoodRequestCreate dto, String userToken) {
        String userId = foodUtils.getUserId(userToken);

        var entity = customFoodMapper.toEntity(dto);

        entity.setUserId(userId);

        return customFoodMapper.toView(repository.save(entity));
    }

    public void delete(String id, String userToken) {
        String userId = foodUtils.getUserId(userToken);

        CustomFoodEntity food = repository
                .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND_WITH_ID, id));

        repository.delete(food);
    }
}
