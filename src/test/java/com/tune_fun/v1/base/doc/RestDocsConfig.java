package com.tune_fun.v1.base.doc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Attributes.Attribute;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@TestConfiguration
@Profile({"test", "test_standalone"})
public class RestDocsConfig {

    @Bean
    public RestDocumentationResultHandler write() {
        return document(
                "{method-name}",
                DocumentUtil.getDocumentRequest(),
                DocumentUtil.getDocumentResponse()
        );
    }

    public static Attribute field(
            final String key,
            final String value) {
        return new Attribute(key, value);
    }

}
