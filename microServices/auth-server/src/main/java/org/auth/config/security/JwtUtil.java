package org.auth.config.security;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import org.auth.UserRepository;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.UserView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;

    @Value("${properties.jwt-secret-keyWord}")
    private String secret;

    public JwtTokenView createJwtToken(UserView user) {

        String token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(Instant.now().plus(Duration.of(12, ChronoUnit.HOURS)))
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withClaim("kilograms", user.getKilograms() != null ? user.getKilograms().toEngineeringString() : null)
                .withClaim("height", user.getHeight() != null ? user.getHeight().toEngineeringString() : null)
                .withClaim("workoutState", user.getWorkoutState() != null ? user.getWorkoutState().name() : null)
                .withClaim("gender", user.getGender() != null ? user.getGender().name() : null)
                .withClaim("userDetails", user.getUserDetails().name())
                .withClaim("age", user.getAge() != null ? user.getAge() : null)
                .sign(Algorithm.HMAC256(secret));

        return new JwtTokenView(token, Instant.now().plus(Duration.of(12, ChronoUnit.HOURS)).toString());
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

    public UserPrincipal convert(DecodedJWT token) {
        return userRepository.findById(Long.parseLong(token.getSubject()))
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("No suck  user exist"));

    }

    public Optional<Long> extractUserId(String token) {
        try {
            token = token.substring(7);
            Map<String, Claim> claimMap = decodeToken(token).getClaims();

            return Optional.ofNullable(claimMap.get("id"))
                    .map(Claim::asLong);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String turnToJwString(String token) {
        return token.substring(7);
    }
}