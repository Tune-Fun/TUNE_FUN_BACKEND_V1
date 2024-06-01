package com.tune_fun.v1.account.application.port.output;

public interface SaveEmailPort {
    void saveEmail(final String email, final String username);

    void clearEmail(final String username);
}
