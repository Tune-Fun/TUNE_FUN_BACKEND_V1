package com.tune_fun.v1.otp.application.port.input.command;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class OtpCommands {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Resend(String username, String otpType) {}

}
