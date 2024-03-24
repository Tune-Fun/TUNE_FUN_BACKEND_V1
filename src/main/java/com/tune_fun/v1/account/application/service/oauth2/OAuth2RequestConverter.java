package com.tune_fun.v1.account.application.service.oauth2;

import com.tune_fun.v1.common.property.AppleProperty;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;

@Component
@RequiredArgsConstructor
public class OAuth2RequestConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private static final String APPLE = "apple";

    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    private final AppleProperty appleProperty;
    private final OAuth2ClientProperties oAuth2ClientProperties;

    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest request) {
        RequestEntity<?> entity = defaultConverter.convert(request);
        String registrationId = request.getClientRegistration().getRegistrationId();

        assert entity != null;
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();

        if (registrationId.contains(APPLE))
            params.set("client_secret", createAppleClientSecret());

        return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
    }

    private String createAppleClientSecret() throws IOException {
        Date expirationDate = Date.from(now().plusDays(30).atZone(systemDefault()).toInstant());

        return Jwts.builder()
                .header().add("kid", appleProperty.keyId())
                .and()
                .issuer(appleProperty.teamId())
                .issuedAt(new Date(currentTimeMillis()))
                .expiration(expirationDate)
                .audience().add("https://appleid.apple.com")
                .and()
                .subject(oAuth2ClientProperties.getRegistration().get(APPLE).getClientId())
                .signWith(getPrivateKey(), Jwts.SIG.ES256)
                .compact();
    }

    public PrivateKey getPrivateKey() throws IOException {
        byte[] oauth2KeyByte = appleProperty.oauth2Key().getBytes(UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(oauth2KeyByte);
        PemReader reader = new PemReader(new InputStreamReader(byteArrayInputStream));

        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(reader.readPemObject());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        return converter.getPrivateKey(privateKeyInfo);
    }

}
