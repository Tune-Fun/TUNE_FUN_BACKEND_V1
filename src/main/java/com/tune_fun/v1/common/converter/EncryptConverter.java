package com.tune_fun.v1.common.converter;

import com.tune_fun.v1.external.aws.kms.KmsProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Converter
@RequiredArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

    private final KmsProvider kmsProvider;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return kmsProvider.encrypt(attribute);
    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String dbData) {
        return kmsProvider.decrypt(dbData);
    }
}
