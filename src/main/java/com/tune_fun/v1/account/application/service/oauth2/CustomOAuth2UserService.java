package com.tune_fun.v1.account.application.service.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tune_fun.v1.account.application.port.output.oauth2.LoadOAuth2InstagramUserInfoPort;
import com.tune_fun.v1.account.domain.value.oauth2.OAuth2Provider;
import com.tune_fun.v1.account.domain.value.oauth2.OAuth2UserInfo;
import com.tune_fun.v1.account.domain.value.oauth2.OAuth2UserInfoFactory;
import com.tune_fun.v1.account.domain.value.oauth2.OAuth2UserPrincipal;
import com.tune_fun.v1.common.exception.OAuth2AuthenticationProcessingException;
import com.tune_fun.v1.common.stereotype.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getUrlDecoder;


@Service
@UseCase
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final ObjectMapper objectMapper;
    private final LoadOAuth2InstagramUserInfoPort loadOAuth2InstagramUserInfoPort;

    @SneakyThrows
    @Override
    public OAuth2UserPrincipal loadUser(final OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2UserRegistration oAuth2UserRegistration = loadRegistration(request);

        try {
            return processOAuth2User(oAuth2UserRegistration);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2UserRegistration loadRegistration(final OAuth2UserRequest request) throws JsonProcessingException {
        final String registrationId = request.getClientRegistration().getRegistrationId();
        final String accessToken = request.getAccessToken().getTokenValue();

        final Map<String, Object> attributes =
                switch (OAuth2Provider.fromRegistrationId(registrationId)) {
                    case APPLE:
                        String idToken = request.getAdditionalParameters().get("id_token").toString();
                        Map<String, Object> payload = decodeJwtTokenPayload(idToken);
                        payload.put("id_token", idToken);
                        yield payload;
                    case INSTAGRAM:
                        yield loadOAuth2InstagramUserInfoPort.loadUserInfo("id,username", accessToken);
                    case null:
                        throw new OAuth2AuthenticationProcessingException("Invalid OAuth2 provider");
                    default:
                        OAuth2User oAuth2User = super.loadUser(request);
                        yield oAuth2User.getAttributes();
                };

        return new OAuth2UserRegistration(registrationId, accessToken, attributes);
    }

    public Map<String, Object> decodeJwtTokenPayload(final String jwtToken) throws JsonProcessingException {
        String[] parts = jwtToken.split("\\.");
        Base64.Decoder decoder = getUrlDecoder();

        byte[] decodedBytes = decoder.decode(parts[1].getBytes(UTF_8));
        String decodedString = new String(decodedBytes, UTF_8);

        return objectMapper.readValue(decodedString, new TypeReference<>() {
        });
    }

    private OAuth2UserPrincipal processOAuth2User(final OAuth2UserRegistration registration) throws NoSuchAlgorithmException {
        final OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registration.registrationId(),
                registration.accessToken(), registration.attributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail()))
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");

        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }

    private record OAuth2UserRegistration(String registrationId, String accessToken, Map<String, Object> attributes) {
    }
}
