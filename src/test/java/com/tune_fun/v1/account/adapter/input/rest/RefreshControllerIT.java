package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class RefreshControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("Access Token 재발급, 성공")
    void refreshSuccess() throws Exception {
        dummyService.initAndLogin();
        String refreshToken = dummyService.getDefaultRefreshToken();

        AccountCommands.Refresh command = new AccountCommands.Refresh(refreshToken);

        FieldDescriptor requestDescriptor = fieldWithPath("refresh_token").description("Refresh Token")
                .attributes(constraint("NOT BLANK"));
        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data").description("Response Data"),
                fieldWithPath("data.access_token").description("Access Token")
        );

        ResultActions refreshResultActions = mockMvc.perform(
                        post(Uris.REFRESH)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(command))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpect(jsonPath("data", notNullValue()))
                .andExpect(jsonPath("data.access_token", notNullValue()));

        assertTrue(validateAccessTokenUseCase.validateAccessToken(getAccessToken(refreshResultActions)));

        refreshResultActions
                .andDo(restDocs.document(
                                requestFields(requestDescriptor), responseFields(responseDescriptors),
                                resource(
                                        builder().
                                                description("Access Token 재발급").
                                                requestFields(requestDescriptor).
                                                responseFields(responseDescriptors)
                                                .build()
                                )
                        )
                );

    }

}
