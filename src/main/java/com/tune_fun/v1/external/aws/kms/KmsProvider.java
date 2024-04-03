package com.tune_fun.v1.external.aws.kms;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tune_fun.v1.common.property.KmsProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;
import software.amazon.cryptography.materialproviders.IKeyring;
import software.amazon.cryptography.materialproviders.MaterialProviders;
import software.amazon.cryptography.materialproviders.model.CreateAwsKmsKeyringInput;
import software.amazon.cryptography.materialproviders.model.MaterialProvidersConfig;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.amazonaws.encryptionsdk.CommitmentPolicy.RequireEncryptRequireDecrypt;
import static com.amazonaws.encryptionsdk.CryptoAlgorithm.ALG_AES_256_GCM_HKDF_SHA512_COMMIT_KEY_ECDSA_P384;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.DAYS;
import static software.amazon.awssdk.core.SdkBytes.fromByteArray;
import static software.amazon.awssdk.services.kms.model.DataKeySpec.AES_256;
import static software.amazon.awssdk.services.kms.model.MacAlgorithmSpec.HMAC_SHA_512;

/**
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html">AWS KMS Concepts</a>
 * @see <a href="https://aws.amazon.com/blogs/security/how-to-protect-hmacs-inside-aws-kms/">How to protect HMACs inside AWS KMS</a>
 * @see <a href="https://docs.aws.amazon.com/ko_kr/encryption-sdk/latest/developer-guide/introduction.html">What is AWS Encryption SDK?</a>
 * @see <a href="https://docs.aws.amazon.com/ko_kr/encryption-sdk/latest/developer-guide/using-keyrings.html">AWS Encryption SDK - Using keyrings</a>
 * @see <a href="https://docs.aws.amazon.com/ko_kr/encryption-sdk/latest/developer-guide/use-kms-keyring.html">AWS Encryption SDK - Use KMS Keyring</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KmsProvider {

    private static final AwsCrypto AWS_ENCRYPTION_SDK = AwsCrypto.builder()
            .withEncryptionAlgorithm(ALG_AES_256_GCM_HKDF_SHA512_COMMIT_KEY_ECDSA_P384)
            .withCommitmentPolicy(RequireEncryptRequireDecrypt)
            .build();

    private final KmsClient kmsClient;
    private final KmsProperty kmsProperty;

    private final Lock lock = new ReentrantLock();
    private LoadingCache<SdkBytes, SdkBytes> cache;

    public byte[] getJwtSignature() {
        GenerateMacRequest request = GenerateMacRequest.builder()
                .macAlgorithm(HMAC_SHA_512)
                .keyId(kmsProperty.jwtSignatureId())
                .message(fromByteArray("AWS KMS - JWT Signature Key".getBytes()))
                .build();
        GenerateMacResponse generateMacResponse = kmsClient.generateMac(request);
        if (generateMacResponse.sdkHttpResponse().isSuccessful())
            return generateMacResponse.mac().asByteArray();

        throw new RuntimeException("Failed to get JWT signature");
    }

    public String encrypt(@NotBlank final String plainText) {
        final IKeyring iKeyring = getAwsKmsKeyring(kmsProperty.encryptKeyId());
        final CryptoResult<byte[], ?> encrypted = AWS_ENCRYPTION_SDK.encryptData(iKeyring, plainText.getBytes(UTF_8));


//        EncryptRequest encryptRequest = EncryptRequest.builder()
//                .keyId(kmsProperty.encryptKeyId())
//                .plaintext(fromByteArray(plainText.getBytes(UTF_8)))
//                .encryptionAlgorithm(EncryptionAlgorithmSpec.SYMMETRIC_DEFAULT)
//                .build();
//
//        EncryptResponse encryptResponse = kmsClient.encrypt(encryptRequest);
//        encryptResponse.

        return new String(encrypted.getResult());
    }

    public String decrypt(@NotBlank final String encryptedText) {
        final IKeyring iKeyring = getAwsKmsKeyring(kmsProperty.encryptKeyId());
        final CryptoResult<byte[], ?> decrypted = AWS_ENCRYPTION_SDK.decryptData(iKeyring, encryptedText.getBytes(UTF_8));
        return new String(decrypted.getResult());
    }

    private IKeyring getAwsKmsKeyring(@NotBlank final String kmsKeyId) {
        final MaterialProviders materialProviders = MaterialProviders.builder()
                .MaterialProvidersConfig(MaterialProvidersConfig.builder().build())
                .build();
        final CreateAwsKmsKeyringInput createAwsKmsKeyringInput = CreateAwsKmsKeyringInput.builder()
                .kmsKeyId(kmsKeyId)
                .kmsClient(kmsClient)
                .build();
        return materialProviders.CreateAwsKmsKeyring(createAwsKmsKeyringInput);
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

    public GenerateDataKeyResponse requestGenerateDataKey() {
        GenerateDataKeyRequest request = GenerateDataKeyRequest.builder()
                .keyId(kmsProperty.encryptKeyId())
                .keySpec(AES_256)
                .build();

        GenerateDataKeyResponse response = kmsClient.generateDataKey(request);

        assert response.sdkHttpResponse().isSuccessful();

        return response;
    }

}
