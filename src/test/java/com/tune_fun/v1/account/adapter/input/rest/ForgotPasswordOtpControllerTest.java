package com.tune_fun.v1.account.adapter.input.rest;

import com.icegreen.greenmail.util.GreenMail;
import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;
import com.tune_fun.v1.otp.application.port.output.LoadOtpPort;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.icegreen.greenmail.util.GreenMailUtil.getBody;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static com.tune_fun.v1.otp.adapter.output.persistence.OtpType.FORGOT_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;


class ForgotPasswordOtpControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private LoadOtpPort loadOtpPort;

    @Autowired
    private VerifyOtpPort verifyOtpPort;

    @Autowired
    private GreenMail greenMail;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("비밀번호 찾기 OTP 발송, 성공")
    void sendForgotPasswordOtpSuccess() throws Exception {
        dummyService.initUser();
        AccountJpaEntity defaultAccount = dummyService.getDefaultAccount();

        String username = dummyService.getDefaultUsername();
        AccountCommands.SendForgotPasswordOtp command = new AccountCommands.SendForgotPasswordOtp(username);

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.FORGOT_PASSWORD_SEND_OTP)
                                .content(toJson(command))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS));

        greenMail.waitForIncomingEmail(1);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals(defaultAccount.getEmail(), receivedMessage.getAllRecipients()[0].toString());
        assertEquals("TuneFun - " + defaultAccount.getNickname() + "님의 인증번호입니다.", receivedMessage.getSubject());


        LoadOtp loadOtpBehavior = new LoadOtp(username, FORGOT_PASSWORD);
        CurrentDecryptedOtp decryptedOtp = loadOtpPort.loadOtp(loadOtpBehavior);

        VerifyOtp verifyOtpBehavior = new VerifyOtp(username, FORGOT_PASSWORD, decryptedOtp.token());
        assertDoesNotThrow(() -> verifyOtpPort.verifyOtp(verifyOtpBehavior));

        FieldDescriptor requestDescriptors = fieldWithPath("username").description("아이디")
                .attributes(constraint("NOT BLANK"));

        resultActions.andDo(restDocs.document(
                        requestFields(requestDescriptors), responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("비밀번호 찾기 OTP 발송").
                                        requestFields(requestDescriptors).
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                )
        );

    }


}
