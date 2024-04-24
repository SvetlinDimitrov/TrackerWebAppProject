package org.trackerwebapp.trackerwebapp.domain.dto.user;

public record JwtResponse(UserView userView, JwtToken accessToken) {}
