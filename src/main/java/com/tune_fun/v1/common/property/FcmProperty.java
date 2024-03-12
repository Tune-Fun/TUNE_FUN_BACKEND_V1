package com.tune_fun.v1.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("fcm")
public record FcmProperty(String sdkFile, String projectId, String accessToken) {}
