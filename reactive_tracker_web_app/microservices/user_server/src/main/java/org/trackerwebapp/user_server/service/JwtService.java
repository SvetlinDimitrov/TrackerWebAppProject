package org.trackerwebapp.user_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.trackerwebapp.user_server.config.UserPrincipal;
import org.trackerwebapp.user_server.config.jwt.JwtToken;
import org.trackerwebapp.user_server.config.jwt.JwtTokenProvider;
import org.trackerwebapp.user_server.domain.dtos.JwtResponse;
import org.trackerwebapp.user_server.domain.dtos.UserView;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtTokenProvider tokenProvider;

  public Mono<JwtResponse> generateJwtToken(UserView user) {
    return Mono.just(user)
        .map(data -> {
          UserPrincipal principal = new UserPrincipal(user.id(), user.email());
          JwtToken token = tokenProvider.createToken(principal);
          return new JwtResponse(user, token);
        });
  }
}
