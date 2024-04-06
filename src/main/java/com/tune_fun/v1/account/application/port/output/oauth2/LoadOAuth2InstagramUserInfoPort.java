package com.tune_fun.v1.account.application.port.output.oauth2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(contextId = "loadOAuth2InstagramUserInfoPort", name = "loadOAuth2InstagramUserInfoPort", url = "${spring.security.oauth2.client.provider.instagram.user-info-uri}")
public interface LoadOAuth2InstagramUserInfoPort {

    @GetMapping
    Map<String, Object> loadUserInfo(@RequestParam(name = "fields") String fields, @RequestParam(name = "access_token") String accessToken);

}
