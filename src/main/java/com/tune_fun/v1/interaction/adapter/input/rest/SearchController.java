package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.Response;
import com.tune_fun.v1.common.response.ResponseMapper;
import com.tune_fun.v1.common.stereotype.WebAdapter;
import com.tune_fun.v1.interaction.application.port.input.usecase.ArtistSearchUseCase;
import com.tune_fun.v1.interaction.application.port.input.usecase.VotePaperSearchUseCase;
import com.tune_fun.v1.interaction.domain.ArtistSearchResult;
import com.tune_fun.v1.interaction.domain.VotePaperSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequiredArgsConstructor
public class SearchController {

    private final ResponseMapper responseMapper;

    private final ArtistSearchUseCase artistSearchUseCase;
    private final VotePaperSearchUseCase votePaperSearchUseCase;

    @GetMapping(value = Uris.SEARCH_ROOT + "/artist")
    public ResponseEntity<Response<ArtistSearchResult>> artistSearch(@RequestParam(name = "last_id", required = false)
                                                                     Integer lastId) {
        ArtistSearchResult homeSearchResult = artistSearchUseCase.searchArtist(lastId);
        return responseMapper.ok(homeSearchResult);
    }

    @GetMapping(value = Uris.SEARCH_ROOT + "/vote-paper")
    public ResponseEntity<Response<VotePaperSearchResult>> votePaperSearch(@RequestParam(name = "last_id", required = false)
                                                                     Integer lastId) {
        VotePaperSearchResult votePaperSearchResult = votePaperSearchUseCase.searchVotePaper(lastId);
        return responseMapper.ok(votePaperSearchResult);
    }


}
