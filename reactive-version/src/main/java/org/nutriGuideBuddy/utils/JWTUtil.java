package org.nutriGuideBuddy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.nutriGuideBuddy.domain.dto.user.JwtToken;
import org.nutriGuideBuddy.domain.entity.UserDetails;
import org.nutriGuideBuddy.domain.enums.UserRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JWTUtil {

  @Value("${jwt.secret}")
  private String secretKeyConfig;

  private Key secretKey;

  @PostConstruct
  public void init() {
    secretKey = Keys.hmacShaKeyFor(secretKeyConfig.getBytes());
  }

  public JwtToken generateToken(UserDetails userDetails) {
    String role = (
        userDetails.getAge() != null && userDetails.getHeight() != null && userDetails.getKilograms() != null
            && userDetails.getGender() != null && userDetails.getWorkoutState() != null
    )
        ? UserRoles.FULLY_REGISTERED.name()
        : UserRoles.NOT_FULLY_REGISTERED.name();

    Date expireAt = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
    return new JwtToken(Jwts.builder()
        .setSubject(userDetails.getUserId())
        .claim("role", role)
        .setExpiration(expireAt) // 1 month expiration
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact(),

        expireAt.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    );
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SignatureException e) {
      log.info("Invalid JWT token: {}", e.getMessage());
    } catch (io.jsonwebtoken.MalformedJwtException e) {
      log.info("Invalid JWT token.");
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
      log.info("Expired JWT token.");
    } catch (io.jsonwebtoken.UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid.");
    }
    return false;
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    String userId = claims.getSubject();
    String role = claims.get("role", String.class);
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
    return new UsernamePasswordAuthenticationToken(userId, "", authorities);
  }
}
