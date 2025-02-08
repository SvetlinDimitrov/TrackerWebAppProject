package org.gateway;

import java.util.HashSet;
import java.util.Set;
import org.example.domain.user.UserView;
import org.springframework.stereotype.Repository;

@Repository
public class GateWayBlockedUsers {

    private final Set<String> blockUser = new HashSet<>();

    public void blockUser(UserView user) {
        blockUser.add(user.id());
    }

    public boolean isUserBlocked(UserView user) {
        return blockUser.contains(user.id());
    }
}
