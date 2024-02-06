package com.habin.demo.account.adapter.output.persistence.jwt;

public sealed interface JwtToken permits AccessToken, RefreshToken {

    String getUsername();

    String getToken();

}
