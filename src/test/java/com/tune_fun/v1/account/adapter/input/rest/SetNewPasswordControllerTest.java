package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.dummy.DummyService;
import com.tune_fun.v1.otp.adapter.output.persistence.OtpType;
import com.tune_fun.v1.otp.domain.state.CurrentDecryptedOtp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class SetNewPasswordControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("비밀번호 재설정, 성공")
    void setNewPasswordSuccess() throws Exception {
        dummyService.initAndLogin();
        dummyService.forgotPasswordOtp();

        CurrentDecryptedOtp forgotPasswordOtp = dummyService.getForgotPasswordOtp();
        dummyService.verifyOtp(OtpType.FORGOT_PASSWORD, forgotPasswordOtp.token());
        String accessToken = dummyService.getDefaultAccessToken();

        String generatedNewPassword = StringUtil.randomAlphaNumericSymbol(15);
        AccountCommands.SetNewPassword command = new AccountCommands.SetNewPassword(generatedNewPassword);

        FieldDescriptor requestDescriptors = fieldWithPath("new_password").description("새로운 비밀번호")
                .attributes(constraint("NOT BLANK"));

        ResultActions resultActions = mockMvc.perform(
                        patch(Uris.SET_NEW_PASSWORD)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(toJson(command))
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS_SET_NEW_PASSWORD));

        newPasswordAssertion(dummyService.getDefaultUsername(), generatedNewPassword);

        resultActions.andDo(restDocs.document(
                        requestFields(requestDescriptors), responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("비밀번호 재설정").
                                        requestFields(requestDescriptors).
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                )
        );

    }

    private void newPasswordAssertion(String username, String newPassword) {
        accountPersistenceAdapter.loadAccountByUsername(username)
                .ifPresentOrElse(a -> assertTrue(passwordEncoder.matches(newPassword, a.getPassword())), Assertions::fail);
    }

}
