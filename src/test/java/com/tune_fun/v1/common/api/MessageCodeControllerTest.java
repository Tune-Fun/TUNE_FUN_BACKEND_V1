package com.tune_fun.v1.common.api;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import com.tune_fun.v1.common.response.MessageCode;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MessageCodeControllerTest extends ControllerBaseTest {

    private static final String MESSAGE_CODE_SNIPPET_FILE = "messagecode-response";

    @Test
    @Order(1)
    void getMessageCodes() throws Exception {
        mockMvc.perform(get(Uris.MESSAGE_CODES).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(restDocs.document(messageCodeResponseFields(fieldDescriptors())));
    }

    private List<FieldDescriptor> fieldDescriptors() {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        for (MessageCode messageCode : MessageCode.values()) {
            FieldDescriptor attributes = fieldWithPath(
                    messageCode.name()).type(JsonFieldType.OBJECT)
                    .attributes(
                            key("code").value(messageCode.getCode()),
                            key("message").value(messageSourceUtil.getMessage(messageCode.getCode())),
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
