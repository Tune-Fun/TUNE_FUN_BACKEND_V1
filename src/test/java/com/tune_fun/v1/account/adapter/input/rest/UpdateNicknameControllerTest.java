package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.dummy.DummyService;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class UpdateNicknameControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapter;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("닉네임, 성공")
    void findUsernameSuccess() throws Exception {
        dummyService.initAndLogin();

        String accessToken = dummyService.getDefaultAccessToken();

        String generatedNewNickname = StringUtil.randomAlphaNumericSymbol(6);
        AccountCommands.UpdateNickname command = new AccountCommands.UpdateNickname(generatedNewNickname);

        FieldDescriptor requestDescriptors = fieldWithPath("new_nickname").description("새로운 닉네임")
                .attributes(constraint("NOT BLANK"));

        ResultActions resultActions = mockMvc.perform(
                        patch(Uris.UPDATE_NICKNAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(toJson(command))
                                .header(AUTHORIZATION, bearerToken(accessToken))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS_UPDATE_NICKNAME));

        newNicknameAssertion(dummyService.getDefaultUsername(), generatedNewNickname);

        resultActions.andDo(restDocs.document(
                        requestFields(requestDescriptors), responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("닉네임 변경").
                                        requestFields(requestDescriptors).
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                )
        );

    }

    private void newNicknameAssertion(String username, String newNickname) {
        accountPersistenceAdapter.loadAccountByUsername(username)
                .ifPresentOrElse(a -> assertEquals(newNickname, a.getNickname()), Assertions::fail);
    }

}
