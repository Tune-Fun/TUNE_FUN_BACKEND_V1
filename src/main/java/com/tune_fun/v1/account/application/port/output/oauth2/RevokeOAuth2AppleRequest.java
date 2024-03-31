package com.tune_fun.v1.account.application.port.output.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RevokeOAuth2AppleRequest(
        String clientId,
        String clientSecret,
        String token
) {
}
