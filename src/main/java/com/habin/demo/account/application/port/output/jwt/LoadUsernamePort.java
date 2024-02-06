package com.habin.demo.account.application.port.output.jwt;

public interface LoadUsernamePort {
    String loadUsernameByToken(final String token);
}
