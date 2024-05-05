package com.tune_fun.v1.account.domain.value.oauth2;

import com.tune_fun.v1.common.util.StringUtil;
import lombok.Getter;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Getter
public final class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String nickname;
    private final String profileImageUrl;

    public GoogleOAuth2UserInfo(String accessToken, Map<String, Object> attributes) throws NoSuchAlgorithmException {
        this.accessToken = accessToken;
        this.attributes = attributes;
        this.id = (String) attributes.get("sub");
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("name");
        this.firstName = (String) attributes.get("given_name");
        this.lastName = (String) attributes.get("family_name");
        this.nickname = StringUtil.generateRandomNickname() + StringUtil.randomNumeric(3);
        this.profileImageUrl = (String) attributes.get("picture");
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }

}
