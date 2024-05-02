package org.nutriGuideBuddy.domain.dto.user;

public record JwtResponse(UserView userView, JwtToken accessToken) {}
