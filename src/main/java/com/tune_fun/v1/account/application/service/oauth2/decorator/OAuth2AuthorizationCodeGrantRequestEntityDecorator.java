package com.tune_fun.v1.account.application.service.oauth2.decorator;

import com.tune_fun.v1.common.property.AppleProperty;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;

import static com.tune_fun.v1.account.domain.state.oauth2.OAuth2Provider.APPLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationCodeGrantRequestEntityDecorator extends OAuth2AuthorizationCodeGrantRequestEntityConverter {

    private final AppleProperty appleProperty;
    private final OAuth2ClientProperties oAuth2ClientProperties;


    @Override
    @SuppressWarnings("unchecked")
    public RequestEntity<?> convert(@NotNull OAuth2AuthorizationCodeGrantRequest request) {
        RequestEntity<?> entity = super.convert(request);

        if (entity == null || entity.getBody() == null)
            throw new IllegalStateException("Conversion resulted in a null entity or body.");

        String registrationId = request.getClientRegistration().getRegistrationId();
        if (!registrationId.contains(APPLE.getRegistrationId())) return entity;

        MultiValueMap<String, String> params;
        try {
            params = (MultiValueMap<String, String>) entity.getBody();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Body of request entity is not of expected type MultiValueMap<String, String>.");
        }

        String clientSecret;
        try {
             clientSecret = createAppleClientSecret();
        } catch (IOException e) {
            throw new IllegalStateException("Error while creating Apple client secret.");
        }

        params.set("client_secret", clientSecret);

        return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
    }

    private String createAppleClientSecret() throws IOException {
        Instant expirationInstant = Instant.now().plusSeconds(30 * 24 * 60 * 60); // 30 days in seconds

        return Jwts.builder()
                .header().add("kid", appleProperty.keyId())
                .and()
                .issuer(appleProperty.teamId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expirationInstant))
                .audience().add("https://appleid.apple.com")
                .and()
                .subject(oAuth2ClientProperties.getRegistration().get(APPLE.getRegistrationId()).getClientId())
                .signWith(getPrivateKey(), Jwts.SIG.ES256)
                .compact();
    }


    /**
     * Retrieves the private key used for signing from a PEM-formatted string.
     *
     * @return The private key.
     * @throws IOException If an error occurs while reading the PEM string.
     */
    public PrivateKey getPrivateKey() throws IOException {
        try (PEMParser pemParser = new PEMParser(new StringReader(appleProperty.oauth2Key()))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
            return converter.getPrivateKey(privateKeyInfo);
        }
    }

}
