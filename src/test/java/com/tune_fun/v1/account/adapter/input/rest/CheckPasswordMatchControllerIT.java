package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.application.port.input.query.AccountQueries;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CheckPasswordMatchControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Test
    @Order(1)
    @Transactional
    @DisplayName("패스워드 일치 여부 확인, 일치")
    void checkPasswordMatchSuccess() throws Exception {
        dummyService.initAccount();
        dummyService.login(dummyService.getDefaultAccount());
        AccountQueries.Password password = new AccountQueries.Password(dummyService.getDefaultPassword());

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.CHECK_PASSWORD_MATCH)
                                .content(toJson(password))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.matched", is(true)));

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("password").description("비밀번호").attributes(constraint("NOT BLANK")),
        };

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data").description("데이터"),
                fieldWithPath("data.matched").description("비밀번호 일치 여부")
        );

        resultActions.andDo(restDocs.document(
                        requestFields(requestDescriptors), responseFields(responseDescriptors),
                        resource(
                                builder().
                                        description("비밀번호 일치 여부 확인").
                                        requestFields(requestDescriptors).
                                        responseFields(responseDescriptors)
                                        .build()
                        )
                )
        );
    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("패스워드 일치 여부 확인, 미일치")
    void checkPasswordMatchFailure() throws Exception {
        dummyService.initAccount();
        dummyService.login(dummyService.getDefaultAccount());
        String defaultPassword = dummyService.getDefaultPassword();
        String wrongPassword = defaultPassword.substring(0, defaultPassword.length() - 1);
        AccountQueries.Password password = new AccountQueries.Password(wrongPassword);

        ResultActions resultActions = mockMvc.perform(
                        post(Uris.CHECK_PASSWORD_MATCH)
                                .content(toJson(password))
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS))
                .andExpect(jsonPath("$.data", notNullValue()))
                .andExpect(jsonPath("$.data.matched", is(false)));

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("password").description("비밀번호").attributes(constraint("NOT BLANK")),
        };

        FieldDescriptor[] responseDescriptors = ArrayUtils.addAll(baseResponseFields,
                fieldWithPath("data").description("데이터"),
                fieldWithPath("data.matched").description("비밀번호 일치 여부")
        );

        resultActions.andDo(restDocs.document(
                        requestFields(requestDescriptors), responseFields(responseDescriptors),
                        resource(
                                builder().
                                        description("비밀번호 일치 여부 확인").
                                        requestFields(requestDescriptors).
                                        responseFields(responseDescriptors)
                                        .build()
                        )
                )
        );
    }
}
