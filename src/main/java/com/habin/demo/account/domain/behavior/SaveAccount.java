package com.habin.demo.account.domain.behavior;

import com.habin.demo.account.application.port.input.command.AccountCommands;

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
