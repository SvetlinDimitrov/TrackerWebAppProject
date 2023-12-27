package org.food.services;

import lombok.RequiredArgsConstructor;
import org.food.exception.InvalidUserTokenHeaderException;
import org.food.repositories.CustomFoodRepository;
import org.food.utils.FoodUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KafkaFoodServiceImp {

    private final CustomFoodRepository customFoodRepository;
    private final FoodUtils foodUtils;
    @Transactional
    @KafkaListener(topics = "USER_DELETION", groupId = "delete", containerFactory = "kafkaListenerUserDeletion")
    public void deleteUser(String userToken) throws InvalidUserTokenHeaderException {
        Long userId = foodUtils.getUserId(userToken);

        customFoodRepository.deleteAllByUserId(userId);
    }
}
