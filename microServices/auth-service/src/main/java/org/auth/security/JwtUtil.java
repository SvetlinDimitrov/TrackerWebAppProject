package org.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.auth.UserRepository;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.UserView;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;

    public JwtTokenView createJwtToken(UserView user) {

        String token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withClaim("kilograms", user.getKilograms() != null ? user.getKilograms().toEngineeringString() : null)
                .withClaim("height", user.getHeight() != null ? user.getHeight().toEngineeringString() : null)
                .withClaim("workoutState", user.getWorkoutState() != null ? user.getWorkoutState().toString() : null)
                .withClaim("gender", user.getGender() != null ? user.getGender().name() : null)
                .withClaim("age", user.getAge() != null ? user.getAge() : null)
                .withClaim("userDetails", user.getUserDetails().name())
                .withExpiresAt(Instant.now().plus(Duration.of(12, ChronoUnit.HOURS)))
                .sign(Algorithm.HMAC256("${properties.jwt-secret-keyWord}"));

        return new JwtTokenView(token , Instant.now().plus(Duration.of(12, ChronoUnit.HOURS)).toString());
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256("${properties.jwt-secret-keyWord}"))
                .build()
                .verify(token);
    }

    public UserDetails convert(DecodedJWT token) {
        return userRepository.findById(Long.parseLong(token.getSubject()))
                .map(UserPrincipal::new)
                .orElseThrow();

    }
}