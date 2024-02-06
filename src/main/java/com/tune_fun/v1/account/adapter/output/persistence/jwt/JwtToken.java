package com.tune_fun.v1.account.adapter.output.persistence.jwt;

public sealed interface JwtToken permits AccessToken, RefreshToken {

    String getUsername();

    String getToken();

}
