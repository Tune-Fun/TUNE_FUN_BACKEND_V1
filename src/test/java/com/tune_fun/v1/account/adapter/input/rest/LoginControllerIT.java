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
import static com.tune_fun.v1.account.adapter.output.persistence.Role.CLIENT_0;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static com.tune_fun.v1.common.util.StringUtil.randomAlphaNumericSymbol;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class LoginControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("로그인, 성공")
    void loginSuccess() throws Exception {
        dummyService.initAccount();

        AccountCommands.Device deviceInfo = new AccountCommands.Device(randomAlphaNumericSymbol(15), randomAlphaNumericSymbol(15));
        AccountCommands.Login command =
                new AccountCommands.Login(dummyService.getDefaultUsername(), dummyService.getDefaultPassword(), deviceInfo);

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.LOGIN)
                                .content(toJson(command))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.username", notNullValue()))
                .andExpect(jsonPath("$.data.roles", allOf(notNullValue(), hasSize(1))))
                .andExpect(jsonPath("$.data.roles[0]", is(CLIENT_0.getAuthority().split("ROLE_")[1])))
                .andExpect(jsonPath("$.data.access_token", notNullValue()))
                .andExpect(jsonPath("$.data.refresh_token", notNullValue()));

        assertTrue(validateAccessTokenUseCase.validateAccessToken(getAccessToken(resultActions)));

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("username").description("아이디").attributes(constraint("NOT BLANK")),
                fieldWithPath("password").description("비밀번호").attributes(constraint("NOT BLANK")),
                fieldWithPath("device").description("로그인 디바이스 정보").attributes(constraint("NOT NULL")),
                fieldWithPath("device.fcm_token").description("FCM 토큰").attributes(constraint("NOT BLANK")),
                fieldWithPath("device.device_token").description("디바이스 토큰").attributes(constraint("NOT BLANK"))
        };

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data").description("데이터"),
                fieldWithPath("data.username").description("아이디"),
                fieldWithPath("data.roles[]").description("권한"),
                fieldWithPath("data.access_token").description("Access Token"),
                fieldWithPath("data.refresh_token").description("Refresh Token")
        );

        resultActions.andDo(restDocs.document(
                        requestFields(requestDescriptors), responseFields(responseDescriptors),
                        resource(
                                builder().
                                        description("로그인").
                                        requestFields(requestDescriptors).
                                        responseFields(responseDescriptors)
                                        .build()
                        )
                )
        );

    }

}
