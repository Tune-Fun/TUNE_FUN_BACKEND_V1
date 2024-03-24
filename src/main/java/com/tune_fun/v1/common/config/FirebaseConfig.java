package com.tune_fun.v1.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import com.tune_fun.v1.common.property.FcmProperty;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@OnlyDevelopmentConfiguration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final FcmProperty fcmProperty;

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(credentialFileStream(fcmProperty.sdkFile())))
                            .setProjectId(fcmProperty.projectId())
                            .build();
            FirebaseApp.initializeApp(options);
            log.info("FirebaseApp initialized.");
        }

    }

    private static ByteArrayInputStream credentialFileStream(final String sdkFile) {
        return new ByteArrayInputStream(sdkFile.getBytes(UTF_8));
    }

}
