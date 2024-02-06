package com.habin.demo.account.application.port.output.jwt;

public interface ValidateJwtTokenPort {
    Boolean validate(final String token);
}
