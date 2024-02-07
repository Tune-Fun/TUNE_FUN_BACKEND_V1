package com.tune_fun.v1.base.doc;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class DocumentUtil {
    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("http")
                        .host("localhost")
                        .port(8080),
                prettyPrint()
        );
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                modifyHeaders()
                        .remove("Transfer-Encoding")
                        .remove("Date")
                        .remove("Keep-Alive")
                        .remove("Connection"),
                prettyPrint()
        );
    }
}
