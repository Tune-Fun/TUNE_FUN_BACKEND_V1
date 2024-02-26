package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

class CheckEmailDuplicateControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    private static final ParameterDescriptor REQUEST_DESCRIPTOR = parameterWithName("email").description("이메일")
            .attributes(constraint("NOT BLANK"));

    @Transactional
    @Test
    @Issue("T1-163")
    @Order(1)
    @DisplayName("이메일 중복확인, 성공")
    void checkEmailDuplicateSuccess() throws Exception {
        dummyService.initAccount();

        mockMvc.perform(
                        get(Uris.CHECK_EMAIL_DUPLICATE)
                                .param("email", "test")
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS_EMAIL_UNIQUE))
                .andDo(
                        restDocs.document(
                                queryParameters(REQUEST_DESCRIPTOR),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("이메일 중복확인").
                                                queryParameters(REQUEST_DESCRIPTOR).
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

        mockMvc.perform(
                        get(Uris.CHECK_EMAIL_DUPLICATE)
                                .param("email", account.getEmail())
                )
                .andExpectAll(baseAssertion(MessageCode.USER_POLICY_EMAIL_REGISTERED))
                .andDo(
                        restDocs.document(
                                queryParameters(REQUEST_DESCRIPTOR),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("이메일 중복확인").
                                                queryParameters(REQUEST_DESCRIPTOR).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );
    }

}
