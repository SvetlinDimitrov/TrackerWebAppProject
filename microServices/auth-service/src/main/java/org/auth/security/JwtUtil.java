package org.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.auth.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;

    private final String secret = "${jwt.secret}";

    public String createJwtToken(Long userId) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(30, ChronoUnit.MINUTES)))
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);


    }

    public UserDetails convert(DecodedJWT token) {
        return userRepository.findById(Long.parseLong(token.getSubject()))
                .map(user ->
                        new User(
                                user.getEmail(),
                                user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserDetails().name())))
                ).orElseThrow();

    }
}