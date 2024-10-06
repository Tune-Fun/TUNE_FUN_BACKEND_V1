package com.tune_fun.v1.account.adapter.output.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tune_fun.v1.account.domain.value.Role;
import com.tune_fun.v1.common.util.ObjectUtil;
import com.tune_fun.v1.common.util.StringUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Set;

@Converter
@RequiredArgsConstructor
public class RoleConverter implements AttributeConverter<Set<Role>, String> {

    private final ObjectUtil objectUtil;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Set<Role> attribute) {
        return objectUtil.objectToJson(attribute);
    }

    @SneakyThrows
    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {

        if (StringUtil.hasText(dbData))
            return objectMapper.readValue(dbData, new TypeReference<>() {
            });

        return null;
    }
}
