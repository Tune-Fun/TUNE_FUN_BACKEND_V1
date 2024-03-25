package com.tune_fun.v1.account.domain.state.oauth2;

import lombok.Getter;

import java.util.Map;

@Getter
public class AppleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String nickname;
    private final String profileImageUrl;

    public AppleOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.attributes = attributes;
        this.id = (String) attributes.get("sub");
        this.email = (String) attributes.get("email");
        this.name = parseName("firstName") + " " + parseName("lastName");
        this.firstName = parseName("firstName");
        this.lastName = parseName("lastName");
        this.nickname = parseName("firstName") + " " + parseName("lastName");
        this.profileImageUrl = null;
    }

    @SuppressWarnings("unchecked")
    private String parseName(final String attr) {
        return ((Map<String, String>) attributes.get("name")).get(attr);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.APPLE;
    }

}
