package com.tune_fun.v1.common.helper;

import com.tune_fun.v1.common.util.EncryptUtil;
import com.tune_fun.v1.external.aws.kms.DataKey;
import com.tune_fun.v1.external.aws.kms.KmsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.tune_fun.v1.common.util.StringUtil.concatWithColon;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;

@Component
@RequiredArgsConstructor
public class KmsEncryptionHelper {

    private final KmsProvider kmsProvider;

    public String encrypt(String plainText) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        DataKey dataKey = kmsProvider.makeDataKey();
        String encryptedText = EncryptUtil.encrypt(dataKey.plainTextKey(), plainText.getBytes(UTF_8));

        return concatWithColon(encodeBase64String(dataKey.encryptedKey()), encryptedText);
    }

    public String decrypt(final String encryptedText) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int keyEnd = encryptedText.indexOf(':');
        byte[] encryptedKey = decodeBase64(encryptedText.substring(0, keyEnd));
        byte[] encryptedToken = decodeBase64(encryptedText.substring(keyEnd + 1));
        byte[] plainText = kmsProvider.requestPlaintextKey(encryptedKey).plaintext().asByteArray();

        return EncryptUtil.decrypt(plainText, encryptedToken);
    }
}
