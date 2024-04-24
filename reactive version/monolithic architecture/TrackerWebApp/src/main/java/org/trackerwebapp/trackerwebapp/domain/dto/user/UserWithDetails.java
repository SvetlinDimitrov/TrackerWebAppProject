package org.trackerwebapp.trackerwebapp.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trackerwebapp.trackerwebapp.domain.entity.UserDetails;
import org.trackerwebapp.trackerwebapp.domain.entity.UserEntity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithDetails {

  private UserEntity user;
  private UserDetails details;
}
