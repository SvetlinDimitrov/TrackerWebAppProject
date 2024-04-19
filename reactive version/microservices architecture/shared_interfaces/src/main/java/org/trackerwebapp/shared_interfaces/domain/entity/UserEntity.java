package org.trackerwebapp.shared_interfaces.domain.entity;

import lombok.Data;

@Data
public class UserEntity {

  private String id;
  private String username;
  private String email;
  private String physicalProfileDetailsId;
}
