package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

class CheckEmailVerifiedControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Transactional
    @Test
    @Order(1)
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


}
