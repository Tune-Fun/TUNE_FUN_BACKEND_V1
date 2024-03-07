package com.tune_fun.v1.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectUtil {

    private final ObjectMapper objectMapper;

    public String objectToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public String objectToPrettyJson(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }


}
