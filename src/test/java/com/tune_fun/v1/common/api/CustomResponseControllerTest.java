package com.tune_fun.v1.common.api;

import com.tune_fun.v1.base.ControllerBaseTest;
import com.tune_fun.v1.common.config.Uris;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomResponseControllerTest extends ControllerBaseTest {

    private static final String CUSTOM_RESPONSE_SNIPPET_FILE = "custom-response";

    private static final String CUSTOM_EXCEPTION_RESPONSE_SNIPPET_FILE = "custom-exception-response";

    @Test
    @Order(1)
    void getCustomResponseExample() throws Exception {
        mockMvc.perform(get(Uris.CUSTOM_RESPONSE_EXAMPLE).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        customResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터").optional()
                        ))
                );
    }

    private static CustomResponseFieldsSnippet customResponseFields(FieldDescriptor... fieldDescriptors) {
        return new CustomResponseFieldsSnippet(CustomResponseControllerTest.CUSTOM_RESPONSE_SNIPPET_FILE, Arrays.asList(fieldDescriptors), true);
    }

}
