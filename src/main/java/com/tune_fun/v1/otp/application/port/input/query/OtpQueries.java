package com.tune_fun.v1.otp.application.port.input.query;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

public class OtpQueries {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Verify(
            @NotBlank(message = "{username.not_blank}")
            String username,
            @NotBlank(message = "{otpType.not_blank}")
            String otpType,
            @NotBlank(message = "{otp.not_blank}")
            String otp
    ) {

    }

}
