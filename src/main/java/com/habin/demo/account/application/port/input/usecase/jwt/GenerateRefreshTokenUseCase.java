package com.habin.demo.account.application.port.input.usecase.jwt;

import org.springframework.security.core.userdetails.UserDetails;

@FunctionalInterface
public interface GenerateRefreshTokenUseCase {
    String generateRefreshToken(final UserDetails userDetails);
}
