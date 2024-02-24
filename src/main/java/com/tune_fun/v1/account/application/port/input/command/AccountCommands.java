package com.tune_fun.v1.account.application.port.input.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Notification(
            Boolean voteProgressNotification,
            Boolean voteEndNotification,
            Boolean voteDeliveryNotification
    ) {
    }

    public record Login(@NotBlank String username, @NotBlank String password) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Refresh(@NotBlank String refreshToken) {
    }

    public record SendForgotPasswordOtp(@NotBlank String username) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SetNewPassword(@NotBlank String newPassword) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateNickname(@NotBlank String newNickname) {
    }

}
