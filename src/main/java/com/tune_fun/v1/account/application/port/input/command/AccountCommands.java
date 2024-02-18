package com.tune_fun.v1.account.application.port.input.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountCommands {

    public record Register(
            @NotBlank
            String username,
            @NotBlank
            String password,
            @NotBlank
            String email,
            @NotBlank
            String nickname,

            @NotNull
            Notification notification
    ) {

    }

    public record Notification(
            Boolean voteProgressNotification,
            Boolean voteEndNotification,
            Boolean voteDeliveryNotification
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
