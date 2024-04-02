package com.tune_fun.v1.account.adapter.input.rest;

import com.tune_fun.v1.account.adapter.output.persistence.device.DeviceJpaEntity;
import com.tune_fun.v1.account.adapter.output.persistence.device.DevicePersistenceAdapter;
import com.tune_fun.v1.account.application.port.input.command.AccountCommands;
import com.tune_fun.v1.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.dummy.DummyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.builder;
import static com.tune_fun.v1.base.doc.RestDocsConfig.constraint;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class LogoutControllerIT extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Autowired
    private ValidateAccessTokenUseCase validateAccessTokenUseCase;

    @Autowired
    private DevicePersistenceAdapter devicePersistenceAdapter;

    @Transactional
    @Test
    @Issue("T1-399")
    @Order(1)
    @DisplayName("로그아웃, 성공")
    void logoutSuccess() throws Exception {
        dummyService.initAndLogin();

        String username = dummyService.getDefaultUsername();
        String accessToken = dummyService.getDefaultAccessToken();
        DeviceJpaEntity device = dummyService.getDefaultDevice();

        AccountCommands.Device deviceCommand = new AccountCommands.Device(device.getFcmToken(), device.getDeviceToken());

        FieldDescriptor[] requestDescriptors = {
                fieldWithPath("fcm_token").description("Fcm Token").attributes(constraint("NOT BLANK")),
                fieldWithPath("device_token").description("Device Token").attributes(constraint("NOT BLANK"))
        };

        ResultActions resultActions = mockMvc.perform(
                        put(Uris.LOGOUT)
                                .header(AUTHORIZATION, bearerToken(accessToken))
                                .content(toJson(deviceCommand))
                                .contentType(APPLICATION_JSON)
                )
                .andExpectAll(baseAssertion(MessageCode.SUCCESS));

        assertFalse(validateAccessTokenUseCase.validateAccessToken(accessToken));
        assertTrue(devicePersistenceAdapter.findByFcmTokenOrDeviceToken(username, device.getFcmToken(), device.getDeviceToken()).isEmpty());

        resultActions
                .andDo(restDocs.document(
                        requestHeaders(authorizationHeader),
                        requestFields(requestDescriptors),
                        responseFields(baseResponseFields),
                        resource(
                                builder().
                                        description("로그아웃").
                                        requestFields(requestDescriptors).
                                        responseFields(baseResponseFields)
                                        .build()
                        )
                ));

    }

}
