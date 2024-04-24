package org.trackerwebapp.trackerwebapp.domain.dto.user;

import java.time.LocalDateTime;

public record JwtToken(String value , LocalDateTime expiresIn){}
