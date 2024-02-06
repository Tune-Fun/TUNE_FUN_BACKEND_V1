package com.habin.demo.account.adapter.output.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.habin.demo.common.util.ObjectUtil;
import com.habin.demo.common.util.StringUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

@Converter
@RequiredArgsConstructor
public class RoleConverter implements AttributeConverter<List<Role>, String> {

    private final ObjectUtil objectUtil;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(List<Role> attribute) {
        return objectUtil.objectToJson(attribute);
    }

    @SneakyThrows
    @Override
    public List<Role> convertToEntityAttribute(String dbData) {

        if (StringUtil.hasText(dbData))
            return objectMapper.readValue(dbData, new TypeReference<>() {
            });

        return null;
    }
}
