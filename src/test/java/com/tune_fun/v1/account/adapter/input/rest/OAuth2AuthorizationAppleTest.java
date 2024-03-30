package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.springframework.restdocs.headers.HeaderDescriptor;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AuthorizationRequestPersistenceAdapter.COOKIE_EXPIRE_SECONDS;
import static com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AuthorizationRequestPersistenceAdapter.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuth2AuthorizationAppleTest extends ControllerBaseTest {

    @Test
    @Issue("T1-177")
    @Order(1)
    @DisplayName("애플 소셜 로그인 페이지 진입, 성공")
    void oauth2AuthorizationAppleSuccess() throws Exception {

        HeaderDescriptor[] responseHeaderDescriptor = {
                headerWithName("Location").description("Redirect URL"),
                headerWithName("Set-Cookie").description("Set-Cookie")
        };

        mockMvc.perform(
                        get(Uris.LOGIN_APPLE)
                )
                .andExpect(status().isFound())
                .andExpectAll(
                        cookie().exists(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME),
                        cookie().maxAge(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, COOKIE_EXPIRE_SECONDS)
                )
                .andDo(restDocs.document(
                        responseHeaders(responseHeaderDescriptor),
                        resource(
                                builder()
                                        .description("애플 소셜 로그인 페이지 진입")
                                        .responseHeaders(responseHeaderDescriptor)
                                        .build()
                        )
                ));

    }

}
