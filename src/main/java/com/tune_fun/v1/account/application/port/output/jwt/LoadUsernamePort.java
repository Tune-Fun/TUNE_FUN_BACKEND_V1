package com.tune_fun.v1.account.application.port.output.jwt;

public interface LoadUsernamePort {
    String loadUsernameByToken(final String token);
}
