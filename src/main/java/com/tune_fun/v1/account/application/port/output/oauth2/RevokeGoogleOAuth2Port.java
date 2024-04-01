package com.tune_fun.v1.account.application.port.output.oauth2;

import retrofit2.http.*;

public interface RevokeGoogleOAuth2Port {

    /**
     * @see <a href="https://developers.google.com/identity/protocols/oauth2/web-server#tokenrevoke">Revoking (invalidating) a token</a>
     * @param accessToken Google OAuth2 access token
     */
    @GET("revoke")
    String revokeOAuth2Google(@Query("token") String accessToken);

}
