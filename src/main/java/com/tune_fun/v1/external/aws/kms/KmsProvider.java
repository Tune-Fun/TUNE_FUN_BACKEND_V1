package com.tune_fun.v1.external.aws.kms;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tune_fun.v1.common.property.KmsProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.DAYS;
import static software.amazon.awssdk.core.SdkBytes.fromByteArray;
import static software.amazon.awssdk.services.kms.model.DataKeySpec.AES_256;
import static software.amazon.awssdk.services.kms.model.MacAlgorithmSpec.HMAC_SHA_512;

/**
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html">AWS KMS Concepts</a>
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#data-keys">Data Keys</a>
 * @see <a href="https://aws.amazon.com/blogs/security/how-to-protect-hmacs-inside-aws-kms/">How to protect HMACs inside AWS KMS</a>
 * @see <a href="https://www.baeldung.com/guava-cacheloader">Introduction to Guava CacheLoader</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KmsProvider {

    private final KmsClient kmsClient;
    private final KmsProperty kmsProperty;

    private final Lock lock = new ReentrantLock();
    private LoadingCache<SdkBytes, SdkBytes> cache;

    private static final long ONE_WEEK_MS = 7 * 24 * 3600 * 1000;

    private DataKey currentKey = null;
    private Date lastKeyTime = null;

    public byte[] getJwtSignature() {
        GenerateMacRequest request = GenerateMacRequest.builder()
                .macAlgorithm(HMAC_SHA_512)
                .keyId(kmsProperty.jwtSignatureArn())
                .message(fromByteArray("AWS KMS - JWT Signature Key".getBytes()))
                .build();
        GenerateMacResponse generateMacResponse = kmsClient.generateMac(request);
        if (generateMacResponse.sdkHttpResponse().isSuccessful())
            return generateMacResponse.mac().asByteArray();

        throw new RuntimeException("Failed to get JWT signature");
    }

    public DataKey makeDataKey() {
        GenerateDataKeyResponse response = requestGenerateDataKey();
        return new DataKey(response.ciphertextBlob().asByteArray(), response.plaintext().asByteArray());
    }

    private LoadingCache<SdkBytes, SdkBytes> plainDataKeyCache() {
        if (cache != null) return cache;

        CacheLoader<SdkBytes, SdkBytes> cacheLoader = new CacheLoader<>() {
            public SdkBytes load(SdkBytes key) {
                DecryptRequest decryptRequest = DecryptRequest.builder()
                        .ciphertextBlob(key)
                        .build();
                DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
                return decryptResponse.plaintext();
            }
        };

        cache = CacheBuilder.newBuilder().
                maximumSize(kmsProperty.dataKeyCacheSize()).
                expireAfterWrite(30, DAYS).
                build(cacheLoader);

        return cache;
    }

//    public DataKey makeDataKey() {
//        DataKey key = getCurrentKey();
//        if (key == null) {
//            key = makeNewDataKey();
//            setCurrentKey(key);
//        }
//        return key;
//    }

//    private DataKey getCurrentKey() {
//        DataKey rkey = null;
//        lock.lock();
//
//        try {
//            Date nowTime = new Date();
//            if (this.currentKey != null && this.lastKeyTime != null)
//                if (this.lastKeyTime.getTime() < nowTime.getTime()) {
//                    long timeDiffInMillis = nowTime.getTime() - this.lastKeyTime.getTime();
//                    if (timeDiffInMillis < ONE_WEEK_MS) rkey = this.currentKey;
//                }
//        } finally {
//            lock.unlock();
//        }
//
//        return rkey;
//    }
//
//    private DataKey makeNewDataKey() {
//        GenerateDataKeyResponse response = requestGenerateDataKey();
//        plainDataKeyCache().put(response.ciphertextBlob(), response.plaintext());
//        return new DataKey(response.ciphertextBlob().asByteArray(), response.plaintext().asByteArray());
//    }
//
//    private void setCurrentKey(DataKey key) {
//        lock.lock();
//        try {
//            this.currentKey = key;
//            this.lastKeyTime = new Date();
//        } finally {
//            lock.unlock();
//        }
//    }

//    public byte[] useDataKey(byte[] encryptedKey) throws ExecutionException {
//        return plainDataKeyCache().get(SdkBytes.fromByteArray(encryptedKey)).asByteArray();
//    }


    private GenerateDataKeyResponse requestGenerateDataKey() {
        GenerateDataKeyRequest request = GenerateDataKeyRequest.builder()
                .keyId(kmsProperty.encryptKeyArn())
                .keySpec(AES_256)
                .build();

        GenerateDataKeyResponse response = kmsClient.generateDataKey(request);

        assert response.sdkHttpResponse().isSuccessful();

        return response;
    }

    public DecryptResponse requestDecryptEncryptedKey(byte[] encryptedKey) {
        DecryptRequest request = DecryptRequest.builder()
                .ciphertextBlob(SdkBytes.fromByteArray(encryptedKey))
                .build();

        DecryptResponse response = kmsClient.decrypt(request);

        assert response.sdkHttpResponse().isSuccessful();
        return response;
    }

}
