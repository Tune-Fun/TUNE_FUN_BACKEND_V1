package com.tune_fun.v1.otp.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.domain.value.CurrentDecryptedOtp;
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
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static com.tune_fun.v1.otp.domain.behavior.OtpType.VERIFY_EMAIL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class VerifyOtpControllerIT extends ControllerBaseTest {

    private static final FieldDescriptor[] REQUEST_DESCRIPTORS = new FieldDescriptor[]{
            fieldWithPath("username").description("아이디").attributes(constraint("NOT BLANK")),
            fieldWithPath("otp_type").description("비밀번호").attributes(constraint("NOT BLANK")),
            fieldWithPath("otp").description("OTP").attributes(constraint("NOT BLANK"))
    };

    @Autowired
    private DummyService dummyService;
    @Autowired
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("OTP 인증, 성공")
    void verifyOtpSuccess() throws Exception {
        dummyService.initAccount();
        dummyService.forgotPasswordOtp();

        CurrentDecryptedOtp forgotPasswordOtp = dummyService.getForgotPasswordOtp();
        OtpQueries.Verify query = new OtpQueries.Verify(forgotPasswordOtp.username(), forgotPasswordOtp.otpType(), forgotPasswordOtp.token());

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data.access_token").description("Access Token"),
                fieldWithPath("data.refresh_token").description("Refresh Token")
        );

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.VERIFY_OTP)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(toJson(query))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS_OTP_VERIFIED));

        assertTrue(validateAccessTokenUseCase.validateAccessToken(getAccessToken(resultActions)));

        resultActions.andDo(
                restDocs.document(
                        requestFields(REQUEST_DESCRIPTORS), responseFields(responseDescriptors),
                        resource(
                                builder().
                                        description("OTP 인증").
                                        requestFields(REQUEST_DESCRIPTORS).
                                        responseFields(responseDescriptors)
                                        .build()
                        )
                )
        );

    }

    @Transactional
    @Test
    @Order(2)
    @DisplayName("OTP 인증, 실패 Case 1. OTP 미존재")
    void verifyOtpFailCase1() throws Exception {
        dummyService.initAccount();
        dummyService.forgotPasswordOtp();

        CurrentDecryptedOtp forgotPasswordOtp = dummyService.getForgotPasswordOtp();
        OtpQueries.Verify query = new OtpQueries.Verify(forgotPasswordOtp.username(), VERIFY_EMAIL.getLabel(), forgotPasswordOtp.token());

        mockMvc.perform(
                        post(Uris.VERIFY_OTP)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(toJson(query))
                )
                .andExpectAll(baseAssertion(MessageCode.EXCEPTION_OTP_NOT_FOUND))
                .andDo(
                        restDocs.document(
                                requestFields(REQUEST_DESCRIPTORS), responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("OTP 인증").
                                                requestFields(REQUEST_DESCRIPTORS).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

    }

}
