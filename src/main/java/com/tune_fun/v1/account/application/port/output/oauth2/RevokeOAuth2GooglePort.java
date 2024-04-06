package com.tune_fun.v1.account.application.port.output.oauth2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "revokeGoogleOAuth2Port", name = "revokeGoogleOAuth2Port", url = "https://oauth2.googleapis.com/revoke")
public interface RevokeOAuth2GooglePort {

    /**
     * @param accessToken Google OAuth2 access token
     * @see <a href="https://developers.google.com/identity/protocols/oauth2/web-server#tokenrevoke">Revoking (invalidating) a token</a>
     */
    @GetMapping
    String revokeOAuth2Google(@RequestParam("token") String accessToken);

}
