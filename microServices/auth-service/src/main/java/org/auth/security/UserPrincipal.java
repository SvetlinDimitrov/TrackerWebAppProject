package org.auth.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.auth.model.entity.User;
import org.auth.model.enums.Gender;
import org.auth.model.enums.WorkoutState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private String password;
    private BigDecimal kilograms;
    private BigDecimal height;
    private Integer age;
    private WorkoutState workoutState;
    private Gender gender;
    private org.auth.model.enums.UserDetails userDetails = org.auth.model.enums.UserDetails.NOT_COMPLETED;

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.kilograms = user.getKilograms();
        this.height = user.getHeight();
        this.age = user.getAge();
        this.workoutState = user.getWorkoutState();
        this.gender = user.getGender();
        this.userDetails = user.getUserDetails();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
