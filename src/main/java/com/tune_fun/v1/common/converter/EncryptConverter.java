package com.tune_fun.v1.common.converter;

import com.tune_fun.v1.external.aws.kms.KmsProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
@RequiredArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

    private final KmsProvider kmsProvider;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String attribute) {
        log.info("convertToDatabaseColumn - encrypting : {}", attribute);
        return kmsProvider.encryptWithKeyRing(attribute);
    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String dbData) {
        log.info("convertToEntityAttribute - decrypting : {}", dbData);
        return kmsProvider.decryptWithKeyRing(dbData);
    }
}
