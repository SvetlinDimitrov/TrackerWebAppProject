package org.auth.infrastructure.security.services;

import org.auth.features.user.entity.User;
import org.auth.infrastructure.security.dto.AuthenticationResponse;

public interface JwtService {

  AuthenticationResponse generateToken(User user);

  Boolean isAccessTokenValid(String token);

  String getEmailFromToken(String token);
}
