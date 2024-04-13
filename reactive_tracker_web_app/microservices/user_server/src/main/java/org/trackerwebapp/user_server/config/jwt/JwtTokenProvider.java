package org.trackerwebapp.user_server.config.jwt;

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
import org.springframework.stereotype.Component;
import org.trackerwebapp.user_server.config.UserPrincipal;
import org.trackerwebapp.user_server.repository.UserRepository;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final UserRepository userRepository;

  @Value("${jwt.secret-word}")
  private String SECRET_WORD;
  @Value("${jwt.valid-duration-time-minutes}")
  private long VALID_DURATION_TIME_MINUTES;
  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    var secret = Base64
        .getEncoder()
        .encodeToString(SECRET_WORD.getBytes());
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public JwtToken createToken(UserPrincipal principal) {

    Claims claims = Jwts.claims().setSubject(principal.getEmail());

    Date now = new Date();
    Date validity = new Date(now.getTime() + VALID_DURATION_TIME_MINUTES);

    String stringValueToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(this.secretKey, SignatureAlgorithm.HS256)
        .compact();

    return new JwtToken(stringValueToken, getTokenExpiration(stringValueToken));
  }

  public Mono<Authentication> getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(this.secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    String userEmail = claims.getSubject();

    return userRepository.findByEmail(userEmail)
        .map(UserPrincipal::new)
        .map(userPrincipal -> new UsernamePasswordAuthenticationToken(userPrincipal, token,
            List.of()));
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

  private LocalDateTime getTokenExpiration(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    Date expirationDate = claims.getExpiration();

    return expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
