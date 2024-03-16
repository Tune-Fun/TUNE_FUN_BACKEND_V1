package com.tune_fun.v1.external.spotify;

import com.tune_fun.v1.common.property.SpotifyProperty;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SpotifyApiMediator {

    private final SpotifyProperty spotifyProperty;

    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(spotifyProperty.clientId())
                .setClientSecret(spotifyProperty.clientSecret())
                .build();
    }

    public String accessToken(SpotifyApi spotifyApi) throws IOException, ParseException, SpotifyWebApiException {
        ClientCredentialsRequest credentialsRequest = spotifyApi.clientCredentials().build();
        final ClientCredentials clientCredentials = credentialsRequest.execute();
        return clientCredentials.getAccessToken();
    }

}
