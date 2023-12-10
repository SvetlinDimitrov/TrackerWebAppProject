package org.auth.config.security;

import java.io.IOException;
import java.util.Optional;

import org.auth.exceptions.UserNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        if (pathMatcher.match("/api/user/register", requestURI) || pathMatcher.match("/api/user/login", requestURI)
            || pathMatcher.match("/swagger-ui.html", requestURI) || pathMatcher.match("/swagger-ui/**", requestURI)
            || pathMatcher.match("/v3/api-docs/**", requestURI) || pathMatcher.match("/v3/api-docs.yaml", requestURI)
            || pathMatcher.match("/swagger-resources/**", requestURI) || pathMatcher.match("/swagger-config/**", requestURI)
            || pathMatcher.match("/webjars/**", requestURI) || pathMatcher.match("/actuator/prometheus", requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String token = extractTokenFromRequest(request)
                    .orElseThrow(() -> new BadCredentialsException("Invalid token"));

            DecodedJWT jwt = jwtUtil.decodeToken(token)
                    .orElseThrow(() -> new BadCredentialsException("Invalid token"));

            UserPrincipal principal = jwtUtil.convert(jwt)
                    .orElseThrow(() -> new UserNotFoundException(token));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null,
                    principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid token");
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
