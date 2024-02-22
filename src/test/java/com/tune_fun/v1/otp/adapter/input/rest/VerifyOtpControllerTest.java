package com.tune_fun.v1.otp.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.otp.application.port.input.query.OtpQueries;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static com.tune_fun.v1.otp.adapter.output.persistence.OtpType.VERIFY_EMAIL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class VerifyOtpControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    private static final FieldDescriptor[] REQUEST_DESCRIPTORS = new FieldDescriptor[]{
            fieldWithPath("username").description("아이디").attributes(constraint("NOT BLANK")),
            fieldWithPath("otp_type").description("비밀번호").attributes(constraint("NOT BLANK")),
            fieldWithPath("otp").description("OTP").attributes(constraint("NOT BLANK"))
    };

    @Transactional
    @Test
    @Order(1)
    @DisplayName("OTP 인증, 성공")
    void verifyOtpSuccess() throws Exception {
        dummyService.initAccount();
        dummyService.forgotPasswordOtp();

        CurrentDecryptedOtp forgotPasswordOtp = dummyService.getForgotPasswordOtp();
        OtpQueries.Verify query = new OtpQueries.Verify(forgotPasswordOtp.username(), forgotPasswordOtp.otpType(), forgotPasswordOtp.token());


        mockMvc.perform(
                        post(Uris.VERIFY_OTP)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(toJson(query))
                )
                .andExpectAll(baseAssertion(MessageCode.OTP_VERIFIED_SUCCESS))
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

    @Transactional
    @Test
    @Order(2)
    @DisplayName("OTP 인증, 실패 Case 1. OTP 미존재")
    void otpVerifyFailCase1() throws Exception {
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
                                                description("OTP 인증, 실패 Case 1. OTP 미존재").
                                                requestFields(REQUEST_DESCRIPTORS).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );

    }

}
