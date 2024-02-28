package com.tune_fun.v1.account.adapter.output.persistence.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuthProvider {
    GOOGLE("google"), APPLE("apple"), INSTAGRAM("instagram");

    private final String value;
}
