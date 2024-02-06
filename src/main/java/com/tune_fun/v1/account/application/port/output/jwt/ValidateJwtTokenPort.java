package com.tune_fun.v1.account.application.port.output.jwt;

public interface ValidateJwtTokenPort {
    Boolean validate(final String token);
}
