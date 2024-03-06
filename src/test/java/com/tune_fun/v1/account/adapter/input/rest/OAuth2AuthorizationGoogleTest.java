package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuth2AuthorizationGoogleTest extends ControllerBaseTest {

    @Autowired
    private LoadAccountPort loadAccountPort;

    @Autowired
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Test
    @Issue("T1-177")
    @Order(1)
    @DisplayName("구글 소셜 로그인, 성공")
    void oauth2AuthorizationGoogleSuccess() throws Exception {
        mockMvc.perform(
                        get(Uris.LOGIN_GOOGLE)
                )
                .andExpect(status().isFound());
    }

}
