package com.tune_fun.v1.music.adapter.output.rest;

import com.tune_fun.v1.external.spotify.SpotifyApiMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpotifyRestAdapter {

    private final SpotifyApiMediator spotifyApiMediator;

}
