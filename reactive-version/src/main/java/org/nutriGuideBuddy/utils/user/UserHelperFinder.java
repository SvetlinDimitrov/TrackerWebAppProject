package org.nutriGuideBuddy.utils.user;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.entity.UserEntity;
import org.nutriGuideBuddy.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHelperFinder {

  private final UserRepository userRepository;

  public Mono<UserEntity> getUser() {
    return ReactiveSecurityContextHolder.getContext()
        .flatMap(securityContext -> {
          Authentication authentication = securityContext.getAuthentication();
          if (authentication instanceof UsernamePasswordAuthenticationToken) {
            String userId = (String) authentication.getPrincipal();
            return userRepository.findUserById(userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))));
          }
          return Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401)));
        });
  }

  public Mono<String> getUserId() {
    return ReactiveSecurityContextHolder.getContext()
        .flatMap(securityContext -> {
          Authentication authentication = securityContext.getAuthentication();
          if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return Mono.just((String) authentication.getPrincipal());
          }
          return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        });
  }
}
