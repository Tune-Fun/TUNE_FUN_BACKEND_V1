package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.interaction.adapter.input.scheduler.LikeCountAggregationScheduler;
import com.tune_fun.v1.interaction.application.port.output.LoadVotePaperLikeCountPort;
import com.tune_fun.v1.vote.application.port.output.SaveVotePaperStatisticsPort;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class LikeControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private LoadVotePaperLikeCountPort loadVotePaperLikeCountPort;

    @SpyBean
    private LikeCountAggregationScheduler likeCountAggregationScheduler;

    @SpyBean
    private SaveVotePaperStatisticsPort saveVotePaperStatPort;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("투표 게시물 좋아요 추가, 성공")
    void likeVotePaperSuccess() throws Exception {
        dummyService.initArtistAndLogin();
        dummyService.initVotePaper();

        dummyService.initAndLogin();

        String accessToken = dummyService.getDefaultAccessToken();

        Long votePaperId = dummyService.getDefaultVotePaper().getId();

        ParameterDescriptor pathParameter = parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL"));

        mockMvc.perform(
                        post(Uris.LIKE_VOTE_PAPER, votePaperId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                pathParameters(pathParameter),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 게시물 좋아요 추가").
                                                pathParameters(pathParameter).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        assertTrue(loadVotePaperLikeCountPort.getVotePaperLikeCountById(votePaperId).isPresent());
        awaitLikeCountAggregationScheduler();
        awaitIncrementLikeCount(votePaperId);
    }

    @Transactional
    @Test
    @Order(2)
    @DisplayName("투표 게시물 좋아요 취소, 성공")
    void unlikeVotePaperSuccess() throws Exception {
        dummyService.initArtistAndLogin();
        dummyService.initVotePaper();
        Long votePaperId = dummyService.getDefaultVotePaper().getId();

        dummyService.initAndLogin();
        dummyService.likeVotePaper(votePaperId, dummyService.getDefaultAccount().getUsername());

        String accessToken = dummyService.getDefaultAccessToken();

        ParameterDescriptor pathParameter = parameterWithName("votePaperId").description("투표 게시물 ID").attributes(constraint("NOT NULL"));

        mockMvc.perform(
                        delete(Uris.LIKE_VOTE_PAPER, votePaperId)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                pathParameters(pathParameter),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("투표 게시물 좋아요 추가").
                                                pathParameters(pathParameter).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        assertTrue(loadVotePaperLikeCountPort.getVotePaperLikeCountById(votePaperId).isPresent());
        awaitLikeCountAggregationScheduler();
        awaitDecrementLikeCount(votePaperId);
    }

    private void awaitLikeCountAggregationScheduler() {
        await().atMost(7, SECONDS).untilAsserted(this::verifyInvokeAggregateLikeCount);
    }

    private void verifyInvokeAggregateLikeCount() {
        verify(likeCountAggregationScheduler, atLeastOnce()).aggregateLikeCount();
    }

    private void awaitIncrementLikeCount(Long votePaperId) {
        await().untilAsserted(() -> verifyInvokeIncrementLikeCount(votePaperId));
    }

    private void verifyInvokeIncrementLikeCount(Long votePaperId) {
        verify(saveVotePaperStatPort, atLeastOnce()).updateLikeCount(votePaperId, 1L);
    }

    private void awaitDecrementLikeCount(Long votePaperId) {
        await().untilAsserted(() -> verifyInvokeDecrementLikeCount(votePaperId));
    }

    private void verifyInvokeDecrementLikeCount(Long votePaperId) {
        verify(saveVotePaperStatPort, atLeastOnce()).updateLikeCount(votePaperId, 0L);
    }
}