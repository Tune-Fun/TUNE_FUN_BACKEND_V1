package com.tune_fun.v1.common.api;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MessageCodeControllerTest extends ControllerBaseTest {

    private static final String MESSAGE_CODE_SNIPPET_FILE = "messagecode-response";

    @Test
    @DisplayName("i18n MessageCode - ko_KR")
    @Order(1)
    void getMessageCodes_ko_KR() throws Exception {
        mockMvc.perform(
                        get(Uris.MESSAGE_CODES)
                                .header(ACCEPT_LANGUAGE, "ko-kr")
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(messageCodeResponseFields(koKRFieldDescriptors())));
    }

    @Test
    @DisplayName("i18n MessageCode - en_US")
    @Order(2)
    void getMessageCodes_en_US() throws Exception {
        mockMvc.perform(
                        get(Uris.MESSAGE_CODES)
                                .header(ACCEPT_LANGUAGE, "en-us")
                                .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(messageCodeResponseFields(enUSFieldDescriptors())));
    }

    private List<FieldDescriptor> koKRFieldDescriptors() {
        return fieldDescriptors(Locale.of("ko", "KR"));
    }

    private List<FieldDescriptor> enUSFieldDescriptors() {
        return fieldDescriptors(Locale.of("en", "US"));
    }

    private List<FieldDescriptor> fieldDescriptors(final Locale locale) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        for (MessageCode messageCode : MessageCode.values()) {
            FieldDescriptor attributes = fieldWithPath(
                    messageCode.name()).type(JsonFieldType.OBJECT)
                    .attributes(
                            key("code").value(messageCode.getCode()),
                            key("message").value(messageSourceUtil.getMessage(messageCode.getCode(), locale)),
                            key("statusCode").value(messageCode.getHttpStatus().value()),
                            key("status").value(messageCode.getHttpStatus().getReasonPhrase())
                    );
            fieldDescriptors.add(attributes);
        }
        return fieldDescriptors;
    }

    private static MessageCodeResponseFieldsSnippet messageCodeResponseFields(List<FieldDescriptor> fieldDescriptors) {
        return new MessageCodeResponseFieldsSnippet(MESSAGE_CODE_SNIPPET_FILE, fieldDescriptors, true);
    }

}
