package org.nutriGuideBuddy.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.nutriGuideBuddy.domain.dto.user.UserWithDetails;
import org.nutriGuideBuddy.domain.entity.UserDetails;

import java.util.List;

@Getter
@Setter
public class UserPrincipal extends User {

  private final String id;

  public UserPrincipal(UserWithDetails data) {
    super(data.getUser().getEmail(),
        data.getUser().getPassword(),
        determineAuthorities(data));
    this.id = data.getUser().getId();
  }

  private static List<GrantedAuthority> determineAuthorities(UserWithDetails data) {
    boolean fullyRegistered = fullyRegistered(data.getDetails());
    return fullyRegistered ? List.of(new SimpleGrantedAuthority("ROLE_FULLY_REGISTERED")) : List.of();
  }

  private static boolean fullyRegistered(UserDetails details) {
    return details != null &&
        details.getAge() != null &&
        details.getHeight() != null &&
        details.getGender() != null &&
        details.getKilograms() != null &&
        details.getWorkoutState() != null;
  }
}
