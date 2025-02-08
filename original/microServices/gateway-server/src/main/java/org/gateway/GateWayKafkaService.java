package org.gateway;

import lombok.RequiredArgsConstructor;
import org.example.domain.user.UserView;
import org.example.util.GsonWrapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GateWayKafkaService {

    private final GsonWrapper gsonWrapper;
    private final GateWayBlockedUsers blockedUsers;

    @KafkaListener(topics = "USER_DELETION", groupId = "gateway_user_deletion", containerFactory = "kafkaListenerUserDeletion")
    public void addNewRecordByUserId(String userToken) {
        var user = gsonWrapper.fromJson(userToken, UserView.class);

        blockedUsers.blockUser(user);
    }

}
