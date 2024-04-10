package com.tune_fun.v1.common.helper;

import com.tune_fun.v1.common.property.AppleProperty;
import com.tune_fun.v1.common.util.EncryptUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;

import static com.tune_fun.v1.account.domain.state.oauth2.OAuth2Provider.APPLE;

@Component
@RequiredArgsConstructor
public class AppleOAuth2ClientSecretHelper {

    private final AppleProperty appleProperty;
    private final OAuth2ClientProperties oAuth2ClientProperties;

    public String createAppleClientSecret() throws IOException {
        Instant expirationInstant = Instant.now().plusSeconds(30 * 24 * 60 * 60); // 30 days in seconds
        final StringReader appleOAuth2KeyReader = new StringReader(appleProperty.oauth2Key());
        final PrivateKey appleOAuth2PrivateKey = EncryptUtil.parsePemAndGetPrivateKey(appleOAuth2KeyReader);


        return Jwts.builder()
                .header().add("kid", appleProperty.keyId())
                .and()
                .issuer(appleProperty.teamId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expirationInstant))
                .audience().add("https://appleid.apple.com")
                .and()
                .subject(oAuth2ClientProperties.getRegistration().get(APPLE.getRegistrationId()).getClientId())
                .signWith(appleOAuth2PrivateKey, Jwts.SIG.ES256)
                .compact();
    }

}
