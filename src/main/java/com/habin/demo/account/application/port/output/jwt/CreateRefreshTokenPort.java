package com.habin.demo.account.application.port.output.jwt;

import com.habin.demo.account.domain.behavior.SaveJwtToken;

public interface CreateRefreshTokenPort {
    String createRefreshToken(final SaveJwtToken saveJwtToken);
}
