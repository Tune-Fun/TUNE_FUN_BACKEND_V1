package com.tune_fun.v1.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tune_fun.v1.common.config.annotation.NotAllowTest;
import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import com.tune_fun.v1.common.property.FcmProperty;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Slf4j
@OnlyDevelopmentConfiguration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final FcmProperty fcmProperty;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fcmProperty.getSdkFile());

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(classPathResource.getInputStream()))
                .setProjectId(fcmProperty.getProjectId())
                .build();

        FirebaseApp.initializeApp(firebaseOptions);

        log.info("FirebaseApp initialized.");
    }

}
