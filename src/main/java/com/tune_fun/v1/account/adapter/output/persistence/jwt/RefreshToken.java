package com.tune_fun.v1.account.adapter.output.persistence.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RefreshToken")
public final class RefreshToken implements Serializable, JwtToken {

    @Id
    private String username;
    private String token;

}
