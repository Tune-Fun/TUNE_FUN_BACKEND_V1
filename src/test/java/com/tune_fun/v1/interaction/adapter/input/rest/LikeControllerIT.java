package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

// TODO Scheduler Redis Connection 문제로 인해 Mock 검증 불가
@Slf4j
class LikeControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

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

    }

}