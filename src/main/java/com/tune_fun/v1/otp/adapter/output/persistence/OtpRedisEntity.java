package com.tune_fun.v1.otp.adapter.output.persistence;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("AccessToken")
public class OtpRedisEntity {

    @Id
    private String username;

    @NotNull
    private String token;

}
