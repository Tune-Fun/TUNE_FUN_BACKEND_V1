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
//        DataKey dataKey = kmsProvider.makeDataKey();

//        log.info("convertToDatabaseColumn - encryptedKey : {}", encodeBase64String(dataKey.encryptedKey()));
//        log.info("convertToDatabaseColumn - plainText : {}", encodeBase64String(dataKey.plainTextKey()));
//
//        String encryptedData = EncryptUtil.encrypt(dataKey.plainTextKey(), attribute.getBytes(UTF_8));
//        return concatWithColon(encodeBase64String(dataKey.encryptedKey()), encryptedData);
//        return kmsProvider.encryptWithKeyRing(attribute);
        return attribute;
    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String dbData) {
        log.info("convertToEntityAttribute - decrypting : {}", dbData);

//        int keyEnd = dbData.indexOf(':');
//
//        byte[] encryptedKey = decodeBase64(dbData.substring(0, keyEnd));
//        byte[] data = decodeBase64(dbData.substring(keyEnd + 1));
//
//
//        byte[] plainText = kmsProvider.requestPlaintextKey(encryptedKey).plaintext().asByteArray();
//
//        log.info("convertToEntityAttribute - encryptedKey : {}", encodeBase64String(encryptedKey));
//        log.info("convertToEntityAttribute - plainText : {}", encodeBase64String(plainText));
//
//        return EncryptUtil.decrypt(plainText, data);
        return kmsProvider.decryptWithKeyRing(dbData);
    }
}
