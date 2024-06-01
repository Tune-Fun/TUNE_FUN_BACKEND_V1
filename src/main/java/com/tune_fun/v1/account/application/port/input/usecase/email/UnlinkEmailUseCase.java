package com.tune_fun.v1.account.application.port.input.usecase.email;

import org.springframework.security.core.userdetails.User;

public interface UnlinkEmailUseCase {
    void unlinkEmail(final User user);
}
