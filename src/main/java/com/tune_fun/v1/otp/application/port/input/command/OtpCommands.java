package com.tune_fun.v1.otp.application.port.input.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

public class OtpCommands {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Resend(@NotBlank(message = "{username.not_blank}") String username,
                         @NotBlank(message = "{otp_type.not_blank}") String otpType) {
    }

}
