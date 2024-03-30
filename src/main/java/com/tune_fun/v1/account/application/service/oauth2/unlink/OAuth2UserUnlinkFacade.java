package com.tune_fun.v1.account.application.service.oauth2.unlink;

import com.tune_fun.v1.account.application.service.oauth2.OAuth2AuthenticationProcessingException;
import com.tune_fun.v1.account.domain.state.oauth2.OAuth2Provider;
import com.tune_fun.v1.external.http.RetrofitClient;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.stereotype.Component;

import static com.tune_fun.v1.account.domain.state.oauth2.OAuth2Provider.APPLE;

@Component
@RequiredArgsConstructor
public class OAuth2UserUnlinkFacade {

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final RevokeOAuth2Gateway revokeOAuth2Gateway = RetrofitClient.getInstance().create(RevokeOAuth2Gateway.class);

    public void unlink(final OAuth2Provider provider, final String accessToken) {
        if (accessToken == null || accessToken.trim().isEmpty())
            throw new OAuth2AuthenticationProcessingException("Access token must not be null or empty");

        switch (provider) {
            case GOOGLE -> revokeOAuth2Gateway.revokeOAuth2Google(accessToken);
            case APPLE -> {
                RevokeOAuth2AppleRequest request = getRevokeOAuth2AppleRequest(accessToken);
                revokeOAuth2Gateway.revokeOAuth2Apple(request);
            }
            default -> throw new OAuth2AuthenticationProcessingException(
                    "Unlink with " + provider.getRegistrationId() + " is not supported");
        }
    }

    @NotNull
    private RevokeOAuth2AppleRequest getRevokeOAuth2AppleRequest(final String accessToken) {
        OAuth2ClientProperties.Registration registration = oAuth2ClientProperties
                .getRegistration()
                .get(APPLE.getRegistrationId());

        if (registration == null) throw new OAuth2AuthenticationProcessingException("Apple registration not found");

        return new RevokeOAuth2AppleRequest(registration.getClientId(), registration.getClientSecret(), accessToken);
    }
}
