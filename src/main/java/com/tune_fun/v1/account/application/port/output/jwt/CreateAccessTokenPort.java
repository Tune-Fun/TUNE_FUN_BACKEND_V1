package com.tune_fun.v1.account.application.port.output.jwt;

import com.tune_fun.v1.account.domain.behavior.SaveJwtToken;

public interface CreateAccessTokenPort {
    String createAccessToken(final SaveJwtToken saveJwtToken);
}
