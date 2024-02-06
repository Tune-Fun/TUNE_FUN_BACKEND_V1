package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.habin.demo.base.ControllerBaseTest;
import com.habin.demo.common.config.Uris;
import com.habin.demo.common.response.MessageCode;
import com.habin.demo.dummy.DummyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class LogoutControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("로그아웃, 성공")
    void logoutSuccess() throws Exception {
        dummyService.initAndLogin();

        String accessToken = dummyService.getDefaultAccessToken();

        ResultActions resultActions = mockMvc.perform(
                        put(Uris.LOGOUT)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS));

        assertFalse(validateAccessTokenUseCase.validateAccessToken(accessToken));

        resultActions
                .andDo(restDocs.document(
                        requestHeaders(authorizationHeader),
                        responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("로그아웃").
                                        requestHeaders(authorizationHeader).
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                ));

    }

}
