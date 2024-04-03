package com.tune_fun.v1.common.util;

import com.tune_fun.v1.external.aws.kms.KmsProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class EncryptUtil implements InitializingBean {

    private static final String BLOCK_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";

    private static final String CIPHER_ALGORITHM2 = "AES/ECB/PKCS5PADDING";

    private SecretKey secretKey;
    private IvParameterSpec ivParameterSpec;

    private final KmsProvider kmsProvider;

    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(BLOCK_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.secretKey = generateKey();
        this.ivParameterSpec = generateIv();
    }

    public String encryptWithKms(@NotNull byte[] key, @NotNull final byte[] plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, BLOCK_ALGORITHM));
        return Base64.encodeBase64String(cipher.doFinal(plainText));
    }

    public String decryptWithKms(@NotNull byte[] key, @NotNull final byte[] cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, BLOCK_ALGORITHM));
        return new String(cipher.doFinal(cipherText));
    }

    public String encrypt(@NotBlank final String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encryptData);
    }

    public String decrypt(@NotBlank final String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decryptData = cipher.doFinal(Base64.decodeBase64(cipherText));
        return new String(decryptData);
    }
}
