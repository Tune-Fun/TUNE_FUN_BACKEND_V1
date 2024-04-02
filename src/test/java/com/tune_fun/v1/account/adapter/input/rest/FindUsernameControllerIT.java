package com.tune_fun.v1.account.adapter.input.rest;

import com.icegreen.greenmail.util.GreenMail;
import com.tune_fun.v1.account.application.port.input.query.AccountQueries;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
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
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class FindUsernameControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private GreenMail greenMail;


    @Transactional
    @Test
    @Order(1)
    @DisplayName("아이디 발송, 성공")
    void findUsernameSuccess() throws Exception {
        dummyService.initAccount();
        String email = dummyService.getDefaultEmail();

        greenMail.purgeEmailFromAllMailboxes();

        AccountQueries.Username query = new AccountQueries.Username(email);
        ResultActions resultActions = mockMvc.perform(
                        post(Uris.FIND_USERNAME)
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(toJson(query))
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS));

        greenMail.waitForIncomingEmail(1);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals(email, receivedMessage.getAllRecipients()[0].toString());
        assertEquals("TuneFun - 아이디 찾기 결과입니다.", receivedMessage.getSubject());

        FieldDescriptor requestDescriptors = fieldWithPath("email").description("이메일")
                .attributes(constraint("NOT BLANK"));

        resultActions.andDo(restDocs.document(
                        requestFields(requestDescriptors), responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("아이디 발송").
                                        requestFields(requestDescriptors).
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                )
        );


    }


}
