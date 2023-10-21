package org.auth.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.auth.UserRepository;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.UserView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;
    @Value("${properties.jwt-secret-keyWord}")
    private String secret;

    public JwtTokenView createJwtToken(UserView user) {

        String token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(Instant.now().plus(Duration.of(12, ChronoUnit.SECONDS)))
                .sign(Algorithm.HMAC256(secret));

        return new JwtTokenView(token , Instant.now().plus(Duration.of(12, ChronoUnit.HOURS)).toString());
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

    public UserPrincipal convert(DecodedJWT token) {
        return userRepository.findById(Long.parseLong(token.getSubject()))
                .map(UserPrincipal::new)
                .orElseThrow( () -> new UsernameNotFoundException("No suck  user exist"));

    }
}