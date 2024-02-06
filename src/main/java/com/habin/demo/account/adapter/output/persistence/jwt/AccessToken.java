package com.habin.demo.account.adapter.output.persistence.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("AccessToken")
public final class AccessToken implements Serializable, JwtToken {

    @Id
    private String username;
    private String token;

}
