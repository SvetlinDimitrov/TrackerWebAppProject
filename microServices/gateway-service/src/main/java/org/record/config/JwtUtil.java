package org.record.config;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    public Map<String, Claim> extractAndVerifyToken(HttpHeaders headers) {

        if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && !authHeader.isEmpty()) {
                String token = authHeader.get(0);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    DecodedJWT decodedJWT = decodeToken(token);
                    return decodedJWT.getClaims();
                }
            }
        }
        throw new RuntimeException(
                "You are not authenticated .Go to /auth/register and /auth/login and get your token");
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256("topSecret12345"))
                .build()
                .verify(token);
    }

}