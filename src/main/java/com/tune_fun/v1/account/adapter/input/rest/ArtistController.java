package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.account.application.port.input.usecase.ArtistSearchUseCase;
import com.tune_fun.v1.interaction.domain.ArtistSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class ArtistController {

    private final ResponseMapper responseMapper;

    private final ArtistSearchUseCase artistSearchUseCase;

    @GetMapping(value = Uris.SEARCH_ROOT + "/artist")
    public ResponseEntity<Response<ArtistSearchResult>> artistSearch(@RequestParam(name = "last_id", required = false)
                                                                     Integer lastId) {
        ArtistSearchResult homeSearchResult = artistSearchUseCase.searchArtist(lastId);
        return responseMapper.ok(homeSearchResult);
    }

}
