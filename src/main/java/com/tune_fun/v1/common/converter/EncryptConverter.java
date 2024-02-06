package com.tune_fun.v1.common.converter;

import com.tune_fun.v1.common.util.EncryptUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Converter
@RequiredArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

    private final EncryptUtil encryptUtil;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptUtil.encrypt(attribute);
    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptUtil.decrypt(dbData);
    }
}
