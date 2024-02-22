package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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

class CheckUsernameDuplicateControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;
    private static final ParameterDescriptor REQUEST_DESCRIPTOR = parameterWithName("username").description("아이디")
            .attributes(constraint("NOT BLANK"));

    @Transactional
    @Test
    @Order(1)
    @DisplayName("아이디 중복확인, 성공 Case 1. 중복되지 않은 아이디")
    void checkUsernameDuplicateSuccessCase1() throws Exception {
        dummyService.initAccount();

        mockMvc.perform(
                        get(Uris.CHECK_USERNAME_DUPLICATE)
                                .param("username", "test")
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andDo(
                        restDocs.document(
                                queryParameters(REQUEST_DESCRIPTOR),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("아이디 중복확인, 성공 Case 1. 중복되지 않은 아이디").
                                                queryParameters(REQUEST_DESCRIPTOR).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );
    }

    @Transactional
    @Test
    @Order(1)
    @DisplayName("아이디 중복확인, 성공 Case 2. 중복된 아이디")
    void checkUsernameDuplicateSuccessCase2() throws Exception {
        dummyService.initAccount();

        mockMvc.perform(
                        get(Uris.CHECK_USERNAME_DUPLICATE)
                                .param("username", dummyService.getDefaultUsername())
                )
                .andExpectAll(baseAssertion(MessageCode.USER_POLICY_USERNAME_REGISTERED))
                .andDo(
                        restDocs.document(
                                queryParameters(REQUEST_DESCRIPTOR),
                                responseFields(baseResponseFields),
                                resource(
                                        builder().
                                                description("아이디 중복확인, 성공 Case 2. 중복된 아이디").
                                                queryParameters(REQUEST_DESCRIPTOR).
                                                responseFields(baseResponseFields)
                                                .build()
                                )
                        )
                );
    }

}