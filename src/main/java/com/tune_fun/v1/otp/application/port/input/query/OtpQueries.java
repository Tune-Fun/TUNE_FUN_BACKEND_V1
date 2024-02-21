package com.tune_fun.v1.otp.application.port.input.query;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OtpQueries {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Verify(
            @NotBlank
            String username,
            @NotNull
            OtpType otpType,
            @NotBlank
            String otp
    ) {

    }

}
