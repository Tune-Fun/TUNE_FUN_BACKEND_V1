package com.tune_fun.v1.account.application.service.oauth2;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.account.domain.state.oauth2.OAuth2UserInfo;
import com.tune_fun.v1.account.domain.state.oauth2.OAuth2UserInfoFactory;
import com.tune_fun.v1.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@XRayEnabled
@Service
@UseCase
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        try {
            return processOAuth2User(request, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(final OAuth2UserRequest request, final OAuth2User oAuth2User) {
        String registrationId = request.getClientRegistration()
                .getRegistrationId();

        String accessToken = request.getAccessToken().getTokenValue();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,
                accessToken,
                oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail()))
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");

        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }
}
