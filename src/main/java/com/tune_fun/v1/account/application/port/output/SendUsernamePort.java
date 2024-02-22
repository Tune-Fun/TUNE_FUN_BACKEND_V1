package com.tune_fun.v1.account.application.port.output;

import com.tune_fun.v1.account.domain.behavior.SendUsername;

public interface SendUsernamePort {
    void sendUsername(SendUsername sendUsername) throws Exception;
}
