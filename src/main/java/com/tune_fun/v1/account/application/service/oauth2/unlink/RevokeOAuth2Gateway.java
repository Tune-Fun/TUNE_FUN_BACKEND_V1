package com.tune_fun.v1.account.application.service.oauth2.unlink;

import retrofit2.http.*;

public interface RevokeOAuth2Gateway {

    /**
     * @see <a href="https://developers.google.com/identity/protocols/oauth2/web-server#tokenrevoke">Revoking (invalidating) a token</a>
     * @param accessToken Google OAuth2 access token
     */
    @GET("https://oauth2.googleapis.com/revoke")
    String revokeOAuth2Google(@Query("token") String accessToken);

    /**
     *
     * @see <a href="https://developer.apple.com/documentation/sign_in_with_apple/revoke_tokens">Revoke Tokens</a>
     * @param request Revokes the access of the client application to the user's data.
     */
    @Multipart
    @POST("https://appleid.apple.com/auth/revoke")
    String revokeOAuth2Apple(@Part final RevokeOAuth2AppleRequest request);

}
