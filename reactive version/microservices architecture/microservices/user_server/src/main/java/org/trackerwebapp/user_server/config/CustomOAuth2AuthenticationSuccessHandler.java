package org.trackerwebapp.user_server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.trackerwebapp.user_server.config.jwt.JwtToken;
import org.trackerwebapp.user_server.config.jwt.JwtTokenProvider;
import org.trackerwebapp.user_server.domain.entity.UserEntity;
import org.trackerwebapp.user_server.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    if (authentication instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
      String email = oauth2Token.getPrincipal().getAttribute("email");
      return userRepository
          .findByEmail(email)
          .switchIfEmpty(Mono.just(new UserEntity())
              .map(user -> {
                user.setEmail(email);
                user.setUsername("User");
                return user;
              })
              .flatMap(userRepository::save))
          .flatMap(user-> {
            JwtToken access_token = jwtTokenProvider.createToken(new User(user.getId(), "", List.of()));
            ServerWebExchange exchange = webFilterExchange.getExchange();
            HttpHeaders requestHeaders = exchange.getResponse().getHeaders();
            requestHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + access_token);
            return onAuthenticationSuccess(webFilterExchange, authentication);
          });
    }
    return onAuthenticationSuccess(webFilterExchange, authentication);
  }
}