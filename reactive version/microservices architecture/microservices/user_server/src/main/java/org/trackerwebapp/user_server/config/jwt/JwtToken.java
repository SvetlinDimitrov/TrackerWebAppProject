package org.trackerwebapp.user_server.config.jwt;

import java.time.LocalDateTime;

public record JwtToken (String value , LocalDateTime expiresIn){}
