package com.tune_fun.v1.common.api;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.io.IOException;
import java.util.List;

public class MessageCodeResponseFieldsSnippet extends AbstractFieldsSnippet {

    public MessageCodeResponseFieldsSnippet(String type, List<FieldDescriptor> descriptors, boolean ignoreUndocumentedFields) {
        super(type, descriptors, null, ignoreUndocumentedFields);
    }

    @Override
    protected MediaType getContentType(Operation operation) {
        return operation.getResponse().getHeaders().getContentType();
    }

    @Override
    protected byte[] getContent(Operation operation) throws IOException {
        return operation.getResponse().getContent();
    }

}
