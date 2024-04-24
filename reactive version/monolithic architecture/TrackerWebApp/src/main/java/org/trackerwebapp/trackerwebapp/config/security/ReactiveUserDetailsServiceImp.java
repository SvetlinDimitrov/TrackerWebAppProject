package org.trackerwebapp.trackerwebapp.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.trackerwebapp.trackerwebapp.repository.UserRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailsServiceImp implements ReactiveUserDetailsService {

  private final UserRepository userRepository;

  @Override
  public Mono<UserDetails> findByUsername(String email) {
    return userRepository.getUserWithDetailsByEmail(email)
        .switchIfEmpty(Mono.error(new UsernameNotFoundException(email)))
        .map(UserPrincipal::new);
  }
}
