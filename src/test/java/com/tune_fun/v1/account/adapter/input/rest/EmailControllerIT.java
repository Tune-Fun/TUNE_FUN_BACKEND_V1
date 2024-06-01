package com.tune_fun.v1.account.adapter.input.rest;

import com.icegreen.greenmail.util.GreenMail;
import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.output.LoadAccountPort;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;
import com.tune_fun.v1.otp.application.port.output.LoadOtpPort;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.value.CurrentDecryptedOtp;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static com.tune_fun.v1.otp.adapter.output.persistence.OtpType.VERIFY_EMAIL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

class EmailControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private LoadAccountPort loadAccountPort;

    @Autowired
    private LoadOtpPort loadOtpPort;

    @Autowired
    private VerifyOtpPort verifyOtpPort;

    @Autowired
    private GreenMail greenMail;

    @Transactional
    @Test
    @Issue("T1-163")
    @Order(1)
    @DisplayName("이메일 중복확인, 성공")
    void checkEmailDuplicateSuccess() throws Exception {
        dummyService.initAccount();

        ParameterDescriptor requestDescriptor = parameterWithName("email").description("이메일")
                .attributes(constraint("NOT BLANK"));

        mockMvc.perform(
                        get(Uris.CHECK_EMAIL_DUPLICATE)
                                .param("email", "test")
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS_EMAIL_UNIQUE))
                .andDo(
                        restDocs.document(
                                queryParameters(requestDescriptor),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("이메일 중복확인").
                                                queryParameters(requestDescriptor).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );
    }

    @Transactional
    @Test
    @Issue("T1-163")
    @Order(2)
    @DisplayName("이메일 중복확인, 실패 - 중복된 이메일")
    void checkEmailDuplicateFailed() throws Exception {
        dummyService.initAccount();
        AccountJpaEntity account = dummyService.getDefaultAccount();

        ParameterDescriptor requestDescriptor = parameterWithName("email").description("이메일")
                .attributes(constraint("NOT BLANK"));

        mockMvc.perform(
                        get(Uris.CHECK_EMAIL_DUPLICATE)
                                .param("email", account.getEmail())
                )
                .andExpectAll(baseAssertion(MessageCode.USER_POLICY_EMAIL_REGISTERED))
                .andDo(
                        restDocs.document(
                                queryParameters(requestDescriptor),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("이메일 중복확인").
                                                queryParameters(requestDescriptor).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );
    }

    @Transactional
    @Test
    @Order(3)
    @DisplayName("이메일 인증 여부 확인, 성공")
    void checkEmailVerifiedSuccess() throws Exception {
        dummyService.initAccount();
        dummyService.forgotPasswordOtp();

        CurrentDecryptedOtp forgotPasswordOtp = dummyService.getForgotPasswordOtp();
        dummyService.verifyOtp(OtpType.FORGOT_PASSWORD, forgotPasswordOtp.token());

        dummyService.login(dummyService.getDefaultAccount());
        String accessToken = dummyService.getDefaultAccessToken();

        mockMvc.perform(
                        get(Uris.CHECK_EMAIL_VERIFIED)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS_EMAIL_VERIFIED))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("이메일 인증 여부 확인").
                                                requestHeaders(authorizationHeader).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

    }

    @Transactional
    @Test
    @Order(4)
    @DisplayName("이메일 등록, 성공")
    void registerEmailSuccess() throws Exception {
        dummyService.initAndLogin();
        dummyService.clearEmail();

        String accessToken = dummyService.getDefaultAccessToken();

        String email = StringUtil.randomAlphabetic(7) + "@" + StringUtil.randomAlphabetic(5) + ".com";
        AccountCommands.SaveEmail command = new AccountCommands.SaveEmail(email);

        FieldDescriptor requestDescriptor = fieldWithPath("email").description("이메일").attributes(constraint("NOT BLANK"));

        mockMvc.perform(
                        post(Uris.EMAIL_ROOT)
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
                                                description("이메일 등록").
                                                requestHeaders(authorizationHeader).
                                                requestFields(requestDescriptor).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        loadAccountPort.currentAccountInfo(dummyService.getDefaultAccount().getUsername())
                .ifPresentOrElse(
                        account -> assertEquals(email, account.email()),
                        () -> Assertions.fail("계정 정보를 찾을 수 없습니다.")
                );
    }

    @Transactional
    @Test
    @Order(5)
    @DisplayName("이메일 인증번호 발송, 성공")
    void verifyEmailSuccess() throws Exception {
        dummyService.initAndLogin();
        AccountJpaEntity defaultAccount = dummyService.getDefaultAccount();
        String accessToken = dummyService.getDefaultAccessToken();

        greenMail.purgeEmailFromAllMailboxes();

        String username = dummyService.getDefaultUsername();

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.VERIFY_EMAIL)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS));


        greenMail.waitForIncomingEmail(1);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals(defaultAccount.getEmail(), receivedMessage.getAllRecipients()[0].toString());
        assertEquals("TuneFun - " + defaultAccount.getNickname() + "님의 인증번호입니다.", receivedMessage.getSubject());

        LoadOtp loadOtpBehavior = new LoadOtp(username, VERIFY_EMAIL.getLabel());
        CurrentDecryptedOtp decryptedOtp = loadOtpPort.loadOtp(loadOtpBehavior);

        VerifyOtp verifyOtpBehavior = new VerifyOtp(username, VERIFY_EMAIL.getLabel(), decryptedOtp.token());
        assertDoesNotThrow(() -> verifyOtpPort.verifyOtp(verifyOtpBehavior));

        resultActions.andDo(
                restDocs.document(
                        requestHeaders(authorizationHeader),
                        responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("이메일 인증번호 발송").
                                        requestHeaders(authorizationHeader).
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                )
        );
    }

    @Transactional
    @Test
    @Order(6)
    @DisplayName("이메일 변경, 성공")
    void changeEmailSuccess() throws Exception {
        dummyService.initAndLogin();

        String accessToken = dummyService.getDefaultAccessToken();

        String email = StringUtil.randomAlphabetic(7) + "@" + StringUtil.randomAlphabetic(5) + ".com";
        AccountCommands.SaveEmail command = new AccountCommands.SaveEmail(email);

        FieldDescriptor requestDescriptor = fieldWithPath("email").description("이메일").attributes(constraint("NOT BLANK"));

        mockMvc.perform(
                        patch(Uris.EMAIL_ROOT)
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
                                                description("이메일 변경").
                                                requestHeaders(authorizationHeader).
                                                requestFields(requestDescriptor).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        loadAccountPort.currentAccountInfo(dummyService.getDefaultAccount().getUsername())
                .ifPresentOrElse(
                        account -> assertEquals(email, account.email()),
                        () -> Assertions.fail("계정 정보를 찾을 수 없습니다.")
                );
    }

    @Transactional
    @Test
    @Order(7)
    @DisplayName("이메일 해제, 성공")
    void unlinkEmailSuccess() throws Exception {
        dummyService.initAndLogin();

        String accessToken = dummyService.getDefaultAccessToken();

        mockMvc.perform(
                        delete(Uris.EMAIL_ROOT)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                requestHeaders(authorizationHeader),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("이메일 해제").
                                                requestHeaders(authorizationHeader).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

        loadAccountPort.currentAccountInfo(dummyService.getDefaultAccount().getUsername())
                .ifPresentOrElse(
                        account -> Assertions.assertNull(account.email()),
                        () -> Assertions.fail("계정 정보를 찾을 수 없습니다.")
                );
    }

}
