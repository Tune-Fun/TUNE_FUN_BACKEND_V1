package com.tune_fun.v1.common.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static javax.crypto.Cipher.*;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64String;

@UtilityClass
public class EncryptUtil {

    private static final String BLOCK_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5PADDING";

    public String encrypt(@NotNull byte[] key, @NotNull final byte[] plainText) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getInstance(CIPHER_ALGORITHM);
        cipher.init(ENCRYPT_MODE, new SecretKeySpec(key, BLOCK_ALGORITHM));
        return encodeBase64String(cipher.doFinal(plainText));
    }

    public String decrypt(@NotNull byte[] key, @NotNull final byte[] cipherText) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getInstance(CIPHER_ALGORITHM);
        cipher.init(DECRYPT_MODE, new SecretKeySpec(key, BLOCK_ALGORITHM));
        return new String(cipher.doFinal(cipherText));
    }
}
