package org.food.features.custom.service;

import static org.food.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND_WITH_ID;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.custom.dto.CustomFilterCriteria;
import org.example.domain.food.custom.dto.CustomFoodRequestCreate;
import org.example.domain.food.custom.dto.CustomFoodView;
import org.example.domain.food.custom.entity.CustomFoodEntity;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.UserExtractor;
import org.food.features.custom.repository.CustomFoodRepository;
import org.food.infrastructure.mappers.CustomFoodMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomFoodServiceImp implements CustomFoodService {

    private final CustomFoodRepository repository;
    private final CustomFoodMapper customFoodMapper;

    public Page<CustomFoodView> getAll(
        String userToken,
        Pageable pageable,
        CustomFilterCriteria filterCriteria
    ) {
        UUID userId = UserExtractor.get(userToken).id();
        return repository.findAllByUserId(userId, pageable ,filterCriteria);
    }

    public CustomFoodView getById(String id, String userToken) {
        UUID userId = UserExtractor.get(userToken).id();

        CustomFoodEntity food = repository
                .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND_WITH_ID, id));

        return customFoodMapper.toView(food);
    }

    public CustomFoodView create(CustomFoodRequestCreate dto, String userToken) {
        UUID userId = UserExtractor.get(userToken).id();

        var entity = customFoodMapper.toEntity(dto);

        entity.setUserId(userId);

        return customFoodMapper.toView(repository.save(entity));
    }

    public void delete(String id, String userToken) {
        UUID userId = UserExtractor.get(userToken).id();

        CustomFoodEntity food = repository
                .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND_WITH_ID, id));

        repository.delete(food);
    }
}
