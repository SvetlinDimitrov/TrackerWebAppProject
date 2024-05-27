package org.nutriGuideBuddy.domain.dto.user;

public record JwtResponse(UserWithDetailsView userView, JwtToken accessToken) {
}
