package com.tune_fun.v1.account.application.port.input.command;

import jakarta.validation.constraints.NotBlank;

public class AccountCommands {

    public record Register(
            @NotBlank
            String username,
            @NotBlank
            String password,
            @NotBlank
            String email,
            @NotBlank
            String nickname
    ) {

    }

    public record Login(
            @NotBlank
            String username,
            @NotBlank
            String password
    ) {

    }

    public record Refresh(
            @NotBlank
            String refreshToken
    ) {

    }

}
