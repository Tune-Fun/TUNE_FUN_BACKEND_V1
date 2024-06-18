package com.tune_fun.v1.interaction.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.interaction.application.port.input.command.InteractionCommands;
import com.tune_fun.v1.interaction.application.port.output.LoadFollowPort;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@Slf4j
class FollowControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private LoadFollowPort loadFollowPort;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("팔로우, 성공")
    void followSuccess() throws Exception {
        dummyService.initAndLogin();
        dummyService.initSecondAccount();

        Long followeeId = dummyService.getDefaultSecondAccount().getId();
        InteractionCommands.Follow command = new InteractionCommands.Follow(followeeId);
        String accessToken = dummyService.getDefaultAccessToken();

        FieldDescriptor requestDescriptor = fieldWithPath("target_account_id").description("팔로우할 대상의 계정 ID").attributes(constraint("NOT NULL"));

        mockMvc.perform(
                        post(Uris.FOLLOW_ROOT)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .content(toJson(command))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                requestFields(requestDescriptor),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("팔로우").
                                                requestFields(requestDescriptor).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        loadFollowPort.loadFollow(followeeId, dummyService.getDefaultAccount().getId())
                .ifPresentOrElse(
                        follow -> log.info("팔로우 성공"),
                        () -> Assertions.fail("팔로우 실패")
                );

    }
}