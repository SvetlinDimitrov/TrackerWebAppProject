package org.gateway.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.gateway.model.UserView;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

    public Optional<UserView> verifyAndExtractUser(HttpHeaders headers) {
        try {
            List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && !authHeader.isEmpty()) {
                String token = authHeader.get(0);
                token = token.substring(7);
                Map<String, Claim> claimMap = decodeToken(token).getClaims();

                return Optional.of(new UserView(
                        claimMap.get("id").asString(),
                        claimMap.get("username").asString(),
                        claimMap.get("email").asString(),
                        claimMap.get("kilograms").asString(),
                        claimMap.get("height").asString(),
                        claimMap.get("workoutState").asString(),
                        claimMap.get("gender").asString(),
                        claimMap.get("userDetails").asString(),
                        claimMap.get("age").asInt()));
            }
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    private DecodedJWT decodeToken(String token) {
        String secret = "topSecret12345";
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

}