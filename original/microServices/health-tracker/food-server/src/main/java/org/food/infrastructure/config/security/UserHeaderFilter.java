package org.food.infrastructure.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.util.GsonWrapper;
import org.example.util.UserExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserHeaderFilter extends OncePerRequestFilter {

  private final GsonWrapper GSON_WRAPPER = new GsonWrapper();

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String userToken = request.getHeader("X-ViewUser");

    if (userToken != null) {
      try {
        var user = UserExtractor.get(userToken);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.role());

        CustomAuthenticationToken authentication = new CustomAuthenticationToken(user,
            List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception e) {
        logger.warn("User not found due to invalid header token", e);
        handleUserNotFoundDueToInvalidHeaderToken(response, e.getMessage());
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  private void handleUserNotFoundDueToInvalidHeaderToken(HttpServletResponse response,
      String message) throws IOException {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
    problemDetail.setTitle("User Not Found Due to Invalid Header Token");

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/json");

    String json = GSON_WRAPPER.toJson(problemDetail);

    response.getWriter().write(json);
  }
}