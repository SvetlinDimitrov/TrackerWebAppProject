package org.trackerwebapp.meal_server.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

  @Value("${jwt.secret-word}")
  private String SECRET_WORD;
  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    var secret = Base64
        .getEncoder()
        .encodeToString(SECRET_WORD.getBytes());
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public Mono<Authentication> getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(this.secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    String userEmail = claims.getSubject();
    User user = new User(userEmail, "", List.of());

    return Mono.just(new UsernamePasswordAuthenticationToken(user, token, List.of()));
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parserBuilder()
          .setSigningKey(this.secretKey)
          .build()
          .parseClaimsJws(token);
      log.info("expiration date: {}", claims.getBody().getExpiration());
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.info("Invalid JWT token: {}", e.getMessage());
      log.trace("Invalid JWT token trace.", e);
    }
    return false;
  }
}
