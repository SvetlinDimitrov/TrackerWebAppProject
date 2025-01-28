package org.auth.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.auth.UserRepository;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.UserView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;

    @Value("${properties.jwt-secret-keyWord}")
    private String secret;

    public JwtTokenView createJwtToken(UserView user) {

        String token = JWT.create()
            .withSubject(String.valueOf(user.id()))
                .withExpiresAt(Instant.now().plus(Duration.of(12, ChronoUnit.HOURS)))
            .withClaim("id", user.id())
            .withClaim("username", user.username())
            .withClaim("email", user.email())
            .withClaim("kilograms",
                user.kilograms() != null ? user.kilograms().toEngineeringString() : null)
            .withClaim("height", user.height() != null ? user.height().toEngineeringString() : null)
            .withClaim("workoutState",
                user.workoutState() != null ? user.workoutState().name() : null)
            .withClaim("gender", user.gender() != null ? user.gender().name() : null)
            .withClaim("userDetails", user.userDetails().name())
            .withClaim("age", user.age() != null ? user.age() : null)
                .sign(Algorithm.HMAC256(secret));

        return new JwtTokenView(token, Instant.now().plus(Duration.of(12, ChronoUnit.HOURS)).toString());
    }

    public Optional<DecodedJWT> decodeToken(String token) {
        return Optional.ofNullable(JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token));
    }

    public Optional<UserPrincipal> convert(DecodedJWT token) {
        return Optional.ofNullable(userRepository.findById(token.getSubject())
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("No suck  user exist")));
    }

    public String extractUserId(String token) {

        token = token.substring(7);
        Map<String, Claim> claimMap = decodeToken(token).get().getClaims();

        return claimMap.get("id").asString();
    }

    public String turnToJwString(String token) {
        return token.substring(7);
    }
}