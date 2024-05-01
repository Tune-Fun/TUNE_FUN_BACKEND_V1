package com.tune_fun.v1.vote.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@Slf4j
class VoteControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Test
    @Order(1)
    @DisplayName("투표, 성공")
    void registerVoteSuccess() throws Exception {
        dummyService.initArtistAndLogin();
        dummyService.initVotePaper();

        dummyService.initAndLogin();
        String accessToken = dummyService.getDefaultAccessToken();

        Long votePaperId = dummyService.getDefaultVotePaper().getId();
        Long voteChoiceId = dummyService.getDefaultVoteChoices().getFirst().getId();

        mockMvc.perform(
                        post(Uris.REGISTER_VOTE, votePaperId, voteChoiceId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS));

    }

}
