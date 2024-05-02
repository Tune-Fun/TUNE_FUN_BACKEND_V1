package com.tune_fun.v1.vote.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.vote.application.port.output.LoadVotePort;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.Optional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@Slf4j
class VoteControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private LoadVotePort loadVotePort;

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

        ParameterDescriptor[] requestDescriptors = {
                parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL")),
                parameterWithName("voteChoiceId").description("투표 선택지 ID").attributes(constraint("NOT NULL"))
        };

        mockMvc.perform(
                        post(Uris.REGISTER_VOTE, votePaperId, voteChoiceId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                pathParameters(requestDescriptors),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 게시물 등록").
                                                pathParameters(requestDescriptors).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );


        Optional<RegisteredVote> registeredVote = loadVotePort.loadVoteByVoterAndVotePaperId(dummyService.getDefaultUsername(), votePaperId);
        assertTrue(registeredVote.isPresent());
    }

}
