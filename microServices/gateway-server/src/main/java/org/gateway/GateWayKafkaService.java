package org.gateway;

import org.gateway.model.UserView;
import org.gateway.utils.GsonWrapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GateWayKafkaService {

    
    private GsonWrapper gsonWrapper;
    private GateWayBlockedUsers blockedUsers;

    @KafkaListener(topics = "USER_DELETION", groupId = "gateway_user_deletion", containerFactory = "kafkaListenerUserDeletion")
    public void addNewRecordByUserId(String userToken) {

        UserView user = gsonWrapper.fromJson(userToken, UserView.class);
       
        blockedUsers.blockUser(user);
    }
}
