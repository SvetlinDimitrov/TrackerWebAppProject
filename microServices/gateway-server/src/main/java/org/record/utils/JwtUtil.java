package org.record.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.record.model.UserView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

    private final String secret = "topSecret12345";

    public Optional<UserView> verifyAndExtractUser(HttpHeaders headers) {
        try {
            List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && !authHeader.isEmpty()) {
                String token = authHeader.get(0);
                token = token.substring(7);
                Map<String, Claim> claimMap = decodeToken(token).getClaims();
                
                return Optional.ofNullable(UserView.builder().id(claimMap.get("id")
                        .asLong()).username(claimMap.get("username").asString())
                        .email(claimMap.get("email").asString())
                        .kilograms(claimMap.get("kilograms").asString())
                        .height(claimMap.get("height").asString())
                        .workoutState(claimMap.get("workoutState").asString())
                        .gender(claimMap.get("gender").asString())
                        .userDetails(claimMap.get("userDetails").asString())
                        .age(claimMap.get("age").asInt()).build());
            }
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

}