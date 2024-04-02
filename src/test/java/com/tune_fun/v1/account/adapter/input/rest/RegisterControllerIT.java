package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.account.adapter.output.persistence.Role.CLIENT_0;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class RegisterControllerIT extends ControllerBaseTest {

    @Autowired
    private LoadAccountPort loadAccountPort;

    @Autowired
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Test
    @Issue("T1-159")
    @Order(1)
    @DisplayName("회원가입, 성공")
    void registerSuccess() throws Exception {
        String username = StringUtil.randomAlphanumeric(10, 15);
        String password = StringUtil.randomAlphaNumericSymbol(15, 20);
        String email = StringUtil.randomAlphabetic(7) + "@" + StringUtil.randomAlphabetic(5) + ".com";
        String nickname = StringUtil.randomAlphabetic(5);

        AccountCommands.Notification notification = new AccountCommands.Notification(true, true, true);
        AccountCommands.Register command = new AccountCommands.Register(username, password, email, nickname, notification);

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.REGISTER)
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

        loadAccountPort.registeredAccountInfoByUsername(username).ifPresentOrElse(
                accountInfo -> assertAll(
                        () -> assertEquals(username, accountInfo.username()),
                        () -> assertTrue(passwordEncoder.matches(password, accountInfo.password())),
                        () -> assertEquals(1, accountInfo.roles().size()),
                        () -> assertEquals(CLIENT_0.name(), accountInfo.roles().stream().toList().getFirst())
                ),
                () -> fail("회원가입 실패")
        );

        assertTrue(validateAccessTokenUseCase.validateAccessToken(getAccessToken(resultActions)));

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("username").description("아이디").attributes(constraint("NOT BLANK")),
                fieldWithPath("password").description("비밀번호").attributes(constraint("NOT BLANK")),
                fieldWithPath("email").description("이메일").attributes(constraint("NOT BLANK")),
                fieldWithPath("nickname").description("닉네임").attributes(constraint("NOT BLANK")),
                fieldWithPath("notification").description("알림 설정").attributes(constraint("NOT NULL")),
                fieldWithPath("notification.vote_progress_notification").description("투표 진행 알림 여부 설정").attributes(constraint("NOT NULL")),
                fieldWithPath("notification.vote_end_notification").description("투표 종료 알림 여부 설정").attributes(constraint("NOT NULL")),
                fieldWithPath("notification.vote_delivery_notification").description("종료된 투표 영상 업로드 알림 여부").attributes(constraint("NOT NULL"))
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
                                        description("회원가입").
                                        requestFields(requestDescriptors).
                                        responseFields(responseDescriptors)
                                        .build()
                        )
                )
        );

    }

}
