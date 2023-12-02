package org.food.services;

import org.food.clients.dto.User;
import org.food.repositories.CustomFoodRepository;
import org.food.utils.GsonWrapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class KafkaFoodServiceImp {

    private final CustomFoodRepository customFoodRepository;
    private final GsonWrapper gsonWrapper;

    @Transactional
    @KafkaListener(topics = "USER_DELETION", groupId = "delete", containerFactory = "kafkaListenerUserDeletion")
    public void deleteUser(String userToken) {
        User user = gsonWrapper.fromJson(userToken, User.class);

        customFoodRepository.deleteAllByUserId(user.getId());
    }
}
