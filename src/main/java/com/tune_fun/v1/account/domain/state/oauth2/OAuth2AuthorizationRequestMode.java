package com.tune_fun.v1.account.domain.state.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuth2AuthorizationRequestMode {
    LOGIN("login"), LINK("link"), UNLINK("unlink");

    private final String queryParameter;

    public static OAuth2AuthorizationRequestMode fromQueryParameter(final String queryParameter) {
        for (OAuth2AuthorizationRequestMode mode : values())
            if (mode.queryParameter.equals(queryParameter)) return mode;
        throw new IllegalArgumentException("No such OAuth2AuthorizationRequestMode: " + queryParameter);
    }
}
