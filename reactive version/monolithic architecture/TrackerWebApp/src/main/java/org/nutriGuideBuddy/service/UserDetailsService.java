package org.nutriGuideBuddy.service;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.user.UserDetailsDto;
import org.nutriGuideBuddy.domain.dto.user.UserDetailsView;
import org.nutriGuideBuddy.repository.UserDetailsRepository;
import org.nutriGuideBuddy.utils.user.UserDetailsModifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDetailsService {

  private final UserDetailsRepository repository;

  public Mono<UserDetailsView> modifyUserDetails(String userId, UserDetailsDto updateDto) {

    if (updateDto == null) {
      return Mono.error(new BadRequestException("Body cannot be empty"));
    }

    return repository.findUserDetailsByUserId(userId)
        .switchIfEmpty(Mono.error(new BadRequestException("No userDetails found")))
        .flatMap(entity -> UserDetailsModifier.ageModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.genderModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.heightModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.kilogramsModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.workoutStateModify(entity, updateDto))
        .flatMap(entity -> repository.updateUserDetails(entity.getId(), entity))
        .map(UserDetailsView::toView)
        .flatMap(this::updateSecurityContextIfNeeded);
  }

  public Mono<UserDetailsView> getByUserId(String userId) {
    return repository.findUserDetailsByUserId(userId)
        .switchIfEmpty(Mono.error(new BadRequestException("No userDetails found")))
        .map(UserDetailsView::toView);
  }

  private Mono<UserDetailsView> updateSecurityContextIfNeeded(UserDetailsView details) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .flatMap(authentication -> {
          if (details.age() != null &&
              details.gender() != null &&
              details.height() != null &&
              details.kilograms() != null &&
              details.workoutState() != null) {

            List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
            authorities.add(new SimpleGrantedAuthority("ROLE_FULLY_REGISTERED"));


            authentication = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                authorities
            );

            org.springframework.security.core.Authentication finalAuthentication = authentication;

            return ReactiveSecurityContextHolder.getContext()
                .doOnNext(securityContext -> securityContext.setAuthentication(finalAuthentication))
                .thenReturn(details);
          } else {
            return Mono.just(details);
          }
        });
  }
}
