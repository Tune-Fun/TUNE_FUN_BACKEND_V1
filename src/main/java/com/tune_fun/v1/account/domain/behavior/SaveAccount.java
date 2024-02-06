package com.tune_fun.v1.account.domain.behavior;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;

/**
 * @see com.habin.demo.account.application.service.RegisterService#register(AccountCommands.Register)
 */
public record SaveAccount(
        String uuid,
        String username,
        String password,
        String email,
        String nickname

) {
}
