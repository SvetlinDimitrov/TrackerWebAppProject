package org.trackerwebapp.user_server.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
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
          User principal = new User(data.id(), "", List.of());
          JwtToken token = tokenProvider.createToken(principal);
          return new JwtResponse(user, token);
        });
  }
}
