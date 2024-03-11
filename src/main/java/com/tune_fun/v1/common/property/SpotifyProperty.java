package com.tune_fun.v1.common.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spotify")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpotifyProperty {

    private String clientId;
    private String clientSecret;

}
