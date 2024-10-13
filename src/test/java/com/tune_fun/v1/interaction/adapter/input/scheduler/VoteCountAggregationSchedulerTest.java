package com.tune_fun.v1.interaction.adapter.input.scheduler;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.vote.application.port.input.usecase.RegisterVoteUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.request.ParameterDescriptor;


import java.util.concurrent.TimeUnit;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class VoteCountAggregationSchedulerTest extends ControllerBaseTest {

    @Autowired
    DummyService dummyService;

    @Autowired
    RegisterVoteUseCase registerVoteUseCase;

    @SpyBean
    VoteCountAggregationScheduler voteCountAggregationScheduler;

    @Test
    @DisplayName("투표수 스케줄러 작동 성공")
    public void VoteCountSchedulerTest() throws Exception{
        //given
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
                                                description("투표 등록").
                                                pathParameters(requestDescriptors).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        await().atMost(20, TimeUnit.SECONDS).untilAsserted(() ->
                verify(voteCountAggregationScheduler, times(4)).aggregateVoteCount());
        //then
        mockMvc.perform(
                        get(Uris.VOTE_PAPER_ROOT + "/{votePaperId}", votePaperId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpect(jsonPath("data.total_vote_count").value(1))
                .andExpect(jsonPath("data.total_like_count").value(0));

    }

}