package com.tune_fun.v1.account.application.port.output.oauth2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(contextId = "revokeAppleOAuth2Port", name = "revokeAppleOAuth2Port", url = "https://oauth2.googleapis.com/revoke")
public interface RevokeOAuth2ApplePort {

    /**
     *
     * @see <a href="https://developer.apple.com/documentation/sign_in_with_apple/revoke_tokens">Revoke Tokens</a>
     * @param request Revokes the access of the client application to the user's data.
     */
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    String revokeOAuth2Apple(@RequestBody final RevokeOAuth2AppleRequest request);

}
