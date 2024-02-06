package com.tune_fun.v1.account.application.port.input.usecase.jwt;

import org.springframework.security.core.userdetails.UserDetails;

@FunctionalInterface
public interface GenerateAccessTokenUseCase {

    String generateAccessToken(final UserDetails userDetails);

}
