package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.dummy.DummyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.headers.HeaderDescriptor;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.account.adapter.output.persistence.oauth2.OAuth2AuthorizationRequestPersistenceAdapter.USERNAME_PARAM_COOKIE_NAME;
import static com.tune_fun.v1.account.domain.value.oauth2.OAuth2AuthorizationRequestMode.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuth2AuthorizationGoogleIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Test
    @Issue("T1-177")
    @Order(1)
    @DisplayName("구글 소셜 로그인 페이지 진입, 성공")
    void oauth2AuthorizationLoginGoogleSuccess() throws Exception {
        HeaderDescriptor[] responseHeaderDescriptor = {
                headerWithName("Location").description("Redirect URL"),
                headerWithName("Set-Cookie").description("Set-Cookie")
        };

        mockMvc.perform(get(Uris.OAUTH2_LOGIN_GOOGLE))
                .andExpect(status().isFound())
                .andExpectAll(oauth2AuthorizationAssertion(LOGIN))
                .andDo(restDocs.document(
                        responseHeaders(responseHeaderDescriptor),
                        resource(
                                builder()
                                        .description("구글 소셜 로그인 페이지 진입")
                                        .responseHeaders(responseHeaderDescriptor)
                                        .build()
                        )
                ));

    }

    @Test
    @Order(2)
    @DisplayName("구글 소셜 계정 연결 페이지 진입, 성공")
    void oauth2AuthorizationLinkGoogleSuccess() throws Exception {
        dummyService.initAccount();

        HeaderDescriptor[] responseHeaderDescriptor = {
                headerWithName("Location").description("Redirect URL"),
                headerWithName("Set-Cookie").description("Set-Cookie")
        };

        mockMvc.perform(get(Uris.OAUTH2_LINK_GOOGLE).queryParam("username", dummyService.getDefaultUsername()))
                .andExpect(status().isFound())
                .andExpectAll(oauth2AuthorizationAssertion(LINK))
                .andExpectAll(
                        cookie().exists(USERNAME_PARAM_COOKIE_NAME),
                        cookie().maxAge(USERNAME_PARAM_COOKIE_NAME, 180)
                )
                .andDo(restDocs.document(
                        responseHeaders(responseHeaderDescriptor),
                        resource(
                                builder()
                                        .description("구글 소셜 계정 연결 페이지 진입")
                                        .responseHeaders(responseHeaderDescriptor)
                                        .build()
                        )
                ));

    }

    @Test
    @Order(3)
    @DisplayName("구글 소셜 계정 연결 해제 페이지 진입, 성공")
    void oauth2AuthorizationUnlinkGoogleSuccess() throws Exception {
        dummyService.initAccount();

        HeaderDescriptor[] responseHeaderDescriptor = {
                headerWithName("Location").description("Redirect URL"),
                headerWithName("Set-Cookie").description("Set-Cookie")
        };

        mockMvc.perform(get(Uris.OAUTH2_UNLINK_GOOGLE).queryParam("username", dummyService.getDefaultUsername()))
                .andExpect(status().isFound())
                .andExpectAll(oauth2AuthorizationAssertion(UNLINK))
                .andExpectAll(
                        cookie().exists(USERNAME_PARAM_COOKIE_NAME),
                        cookie().maxAge(USERNAME_PARAM_COOKIE_NAME, 180)
                )
                .andDo(restDocs.document(
                        responseHeaders(responseHeaderDescriptor),
                        resource(
                                builder()
                                        .description("구글 소셜 계정 연결 해제 페이지 진입")
                                        .responseHeaders(responseHeaderDescriptor)
                                        .build()
                        )
                ));

    }

}
