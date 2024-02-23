package com.tune_fun.v1.otp.application.port.input.query;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

public class OtpQueries {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Verify(
            @NotBlank
            String username,
            @NotBlank
            String otpType,
            @NotBlank
            String otp
    ) {

    }

}
