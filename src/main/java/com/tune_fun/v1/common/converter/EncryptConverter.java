package com.tune_fun.v1.common.converter;

import com.tune_fun.v1.common.util.EncryptUtil;
import com.tune_fun.v1.external.aws.kms.DataKey;
import com.tune_fun.v1.external.aws.kms.KmsProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.tune_fun.v1.common.util.StringUtil.concatWithColon;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;

@Slf4j
@Converter
@RequiredArgsConstructor
public class EncryptConverter implements AttributeConverter<String, String> {

    private final KmsProvider kmsProvider;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(String attribute) {
        DataKey dataKey = kmsProvider.makeDataKey();

        log.info("convertToDatabaseColumn - encrypting : {}", attribute);
        log.info("convertToDatabaseColumn - encryptedKey : {}", encodeBase64String(dataKey.encryptedKey()));
        log.info("convertToDatabaseColumn - plainText : {}", encodeBase64String(dataKey.plainTextKey()));

        String encryptedData = EncryptUtil.encrypt(dataKey.plainTextKey(), attribute.getBytes(UTF_8));
        return concatWithColon(encodeBase64String(dataKey.encryptedKey()), encryptedData);
    }

    @SneakyThrows
    @Override
    public String convertToEntityAttribute(String dbData) {
        log.info("convertToEntityAttribute - decrypting : {}", dbData);

        int keyEnd = dbData.indexOf(':');

        byte[] encryptedKey = decodeBase64(dbData.substring(0, keyEnd));
        byte[] data = decodeBase64(dbData.substring(keyEnd + 1));


        byte[] plainText = kmsProvider.requestPlaintextKey(encryptedKey).plaintext().asByteArray();

        log.info("convertToEntityAttribute - encryptedKey : {}", encodeBase64String(encryptedKey));
        log.info("convertToEntityAttribute - plainText : {}", encodeBase64String(plainText));

        return EncryptUtil.decrypt(plainText, data);
    }
}
