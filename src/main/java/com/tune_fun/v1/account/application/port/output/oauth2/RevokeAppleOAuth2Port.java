package com.tune_fun.v1.account.application.port.output.oauth2;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RevokeAppleOAuth2Port {

    /**
     *
     * @see <a href="https://developer.apple.com/documentation/sign_in_with_apple/revoke_tokens">Revoke Tokens</a>
     * @param request Revokes the access of the client application to the user's data.
     */
    @Multipart
    @POST("revoke")
    String revokeOAuth2Apple(@Part final RevokeOAuth2AppleRequest request);

}
