package com.tune_fun.v1.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spotify")
public record SpotifyProperty(String clientId, String clientSecret) {}
