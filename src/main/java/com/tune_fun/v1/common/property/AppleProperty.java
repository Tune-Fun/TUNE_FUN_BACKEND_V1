package com.tune_fun.v1.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("apple")
public record AppleProperty(String keyId, String teamId) {
}
