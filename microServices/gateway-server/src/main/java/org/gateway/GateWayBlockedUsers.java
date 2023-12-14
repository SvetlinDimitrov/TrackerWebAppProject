package org.gateway;

import java.util.HashSet;
import java.util.Set;

import org.gateway.model.UserView;
import org.springframework.stereotype.Repository;

@Repository
public class GateWayBlockedUsers {

    private Set<UserView> blockUser = new HashSet<>();

    public void blockUser(UserView user) {
        blockUser.add(user);
    }
    public boolean isUserBlocked(UserView user) {
        return blockUser.contains(user);
    }
}
