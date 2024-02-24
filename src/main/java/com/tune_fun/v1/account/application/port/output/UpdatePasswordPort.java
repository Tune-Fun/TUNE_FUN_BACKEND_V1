package com.tune_fun.v1.account.application.port.output;

public interface UpdatePasswordPort {
    void updatePassword(final String username, final String encodedPassword);
}
