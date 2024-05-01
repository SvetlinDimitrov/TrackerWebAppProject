package org.nutriGuideBuddy.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nutriGuideBuddy.domain.entity.UserDetails;
import org.nutriGuideBuddy.domain.entity.UserEntity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithDetails {

  private UserEntity user;
  private UserDetails details;
}
