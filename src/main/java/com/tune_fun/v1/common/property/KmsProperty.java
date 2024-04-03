package com.tune_fun.v1.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kms")
public record KmsProperty(
        String jwtSignatureArn,
        String encryptKeyArn,
        Integer dataKeyCacheSize
) {
}
