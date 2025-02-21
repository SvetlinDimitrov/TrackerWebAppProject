package org.food.infrastructure.kafka.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.util.UserExtractor;
import org.food.features.custom.repository.CustomFoodRepository;
import org.food.infrastructure.utils.FoodUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomKafkaFoodServiceImp {

    private final CustomFoodRepository customFoodRepository;

    @Transactional
    @KafkaListener(topics = "USER_DELETION", groupId = "delete", containerFactory = "kafkaListenerUserDeletion")
    public void deleteUser(String userToken) {
        UUID userId = UserExtractor.get(userToken).id();

        customFoodRepository.deleteAllByUserId(userId);
    }
}
