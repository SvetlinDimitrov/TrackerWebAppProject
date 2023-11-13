package org.auth.config.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTDecodeException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        List<String> allowedUriToHitWithoutHavingJwtToken = List.of("/api/user/login", "/api/user/register");

        if (allowedUriToHitWithoutHavingJwtToken.stream().anyMatch(uri -> uri.equals(request.getRequestURI()))) {
            filterChain.doFilter(request, response);
        } else {
            try {
                extractTokenFromRequest(request)
                        .map(jwtUtil::decodeToken)
                        .map(jwtUtil::convert)
                        .map(t -> new UsernamePasswordAuthenticationToken(t, null, t.getAuthorities()))
                        .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

                filterChain.doFilter(request, response);
            } catch (JWTDecodeException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
