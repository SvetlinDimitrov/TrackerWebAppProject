package org.trackerwebapp.user_server.domain.dtos;

import org.trackerwebapp.user_server.domain.entity.UserEntity;

public record UserView(String id, String username, String email,
                       PhysicalProfileDetailsView detailsView) {

  public static UserView fromEntity(UserEntity userEntity,
      PhysicalProfileDetailsView detailsView) {
    return new UserView(
        userEntity.getId().toString(),
        userEntity.getUsername(),
        userEntity.getEmail(),
        detailsView
    );
  }
}
