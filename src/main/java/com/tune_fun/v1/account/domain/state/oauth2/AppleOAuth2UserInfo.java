package com.tune_fun.v1.account.domain.state.oauth2;

import com.tune_fun.v1.common.util.StringUtil;
import lombok.Getter;

import java.security.NoSuchAlgorithmException;
import java.util.Map;


@Getter
public final class AppleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String nickname;
    private final String profileImageUrl;

    public AppleOAuth2UserInfo(String accessToken, Map<String, Object> attributes) throws NoSuchAlgorithmException {
        this.accessToken = accessToken;
        this.attributes = attributes;
        this.id = (String) attributes.get("sub");
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("email");
        this.firstName = null;
        this.lastName = null;
        this.nickname = StringUtil.generateRandomNickname() + StringUtil.randomNumeric(3);
        this.profileImageUrl = null;
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.APPLE;
    }

}
