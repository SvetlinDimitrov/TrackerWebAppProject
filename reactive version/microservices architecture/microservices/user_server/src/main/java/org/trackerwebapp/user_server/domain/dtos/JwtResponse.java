package org.trackerwebapp.user_server.domain.dtos;

import org.trackerwebapp.user_server.config.jwt.JwtToken;

public record JwtResponse(UserView userView, JwtToken accessToken) {}
