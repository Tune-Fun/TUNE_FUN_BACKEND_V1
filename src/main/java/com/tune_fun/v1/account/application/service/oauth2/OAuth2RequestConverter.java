package com.tune_fun.v1.account.application.service.oauth2;

import com.tune_fun.v1.common.property.AppleProperty;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2RequestConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private static final String APPLE = "apple";

    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    private final AppleProperty appleProperty;
    private final OAuth2ClientProperties oAuth2ClientProperties;

    // TODO : APPLE 로그인시, OAuth2AuthorizationCodeGrantRequest.authorizationExchange.authorizationResponse 에서 username을 가지고 오지 못함
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
        PEMParser pemParser = new PEMParser(new StringReader(appleProperty.oauth2Key()));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());

        pemParser.close();
        return converter.getPrivateKey(privateKeyInfo);
    }

}
