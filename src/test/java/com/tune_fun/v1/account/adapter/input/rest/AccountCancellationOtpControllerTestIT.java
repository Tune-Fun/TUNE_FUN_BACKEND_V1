package com.tune_fun.v1.account.adapter.input.rest;

import com.icegreen.greenmail.util.GreenMail;
import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.otp.application.port.output.LoadOtpPort;
import com.tune_fun.v1.otp.application.port.output.VerifyOtpPort;
import com.tune_fun.v1.otp.domain.behavior.LoadOtp;
import com.tune_fun.v1.otp.domain.behavior.VerifyOtp;
import com.tune_fun.v1.otp.domain.value.CurrentDecryptedOtp;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.otp.domain.behavior.OtpType.ACCOUNT_CANCELLATION;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class AccountCancellationOtpControllerTestIT extends ControllerBaseTest {

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
    @DisplayName("계정 해지 OTP 전송, 성공")
    void sendAccountCancellationOtpSuccess() throws Exception {
        dummyService.initAccount();
        AccountJpaEntity defaultAccount = dummyService.getDefaultAccount();
        dummyService.login(defaultAccount);

        greenMail.purgeEmailFromAllMailboxes();

        String username = dummyService.getDefaultUsername();

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.ACCOUNT_CANCELLATION_SEND_OTP)
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS_ACCOUNT_CANCELLATION_OTP_SENT));

        greenMail.waitForIncomingEmail(1);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals(defaultAccount.getEmail(), receivedMessage.getAllRecipients()[0].toString());
        assertEquals("TuneFun - " + defaultAccount.getNickname() + "님의 인증번호입니다.", receivedMessage.getSubject());


        LoadOtp loadOtpBehavior = new LoadOtp(username, ACCOUNT_CANCELLATION.getLabel());
        CurrentDecryptedOtp decryptedOtp = loadOtpPort.loadOtp(loadOtpBehavior);

        VerifyOtp verifyOtpBehavior = new VerifyOtp(username, ACCOUNT_CANCELLATION, decryptedOtp.token());
        assertDoesNotThrow(() -> verifyOtpPort.verifyOtp(verifyOtpBehavior));

        resultActions.andDo(restDocs.document(
                        responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("계정 해지 OTP 발송").
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                )
        );

    }

}