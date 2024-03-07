package com.tune_fun.v1.common.config;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import com.tune_fun.v1.common.property.FcmProperty;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Date;

import static java.lang.Long.MAX_VALUE;

@Slf4j
@OnlyDevelopmentConfiguration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final FcmProperty fcmProperty;

    @PostConstruct
    public void init() throws IOException {
        AccessToken googleOAuth2AccessToken = new AccessToken(fcmProperty.getAccessToken(), new Date(MAX_VALUE));
        GoogleCredentials googleCredentials = GoogleCredentials.create(googleOAuth2AccessToken);

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .setProjectId(fcmProperty.getProjectId())
                .build();

        FirebaseApp.initializeApp(firebaseOptions);

        log.info("FirebaseApp initialized.");
    }

}
