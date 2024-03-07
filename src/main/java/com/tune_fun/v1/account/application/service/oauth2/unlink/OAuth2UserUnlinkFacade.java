package com.tune_fun.v1.account.application.service.oauth2.unlink;

import com.tune_fun.v1.account.application.service.oauth2.OAuth2AuthenticationProcessingException;
import com.tune_fun.v1.account.domain.state.oauth2.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkFacade {

    private final GoogleOAuth2UserUnlink googleOAuth2UserUnlink;
    private final AppleOAuth2UserUnlink appleOAuth2UserUnlink;

    public void unlink(OAuth2Provider provider, String accessToken) {
        switch (provider) {
            case GOOGLE:
                googleOAuth2UserUnlink.unlink(accessToken);
                break;
            case APPLE:
                appleOAuth2UserUnlink.unlink(accessToken);
                break;
            default:
                throw new OAuth2AuthenticationProcessingException(
                        "Unlink with " + provider.getRegistrationId() + " is not supported");
        }

    }
}
