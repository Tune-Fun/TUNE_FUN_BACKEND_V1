package com.tune_fun.v1.external.aws.kms;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.tune_fun.v1.common.property.KmsProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;
import software.amazon.cryptography.materialproviders.IKeyring;
import software.amazon.cryptography.materialproviders.MaterialProviders;
import software.amazon.cryptography.materialproviders.model.CreateAwsKmsMultiKeyringInput;
import software.amazon.cryptography.materialproviders.model.MaterialProvidersConfig;

import java.util.Collections;
import java.util.Map;

import static com.amazonaws.encryptionsdk.internal.Utils.decodeBase64String;
import static com.amazonaws.encryptionsdk.internal.Utils.encodeBase64String;
import static java.nio.charset.StandardCharsets.UTF_8;
import static software.amazon.awssdk.core.SdkBytes.fromByteArray;
import static software.amazon.awssdk.services.kms.model.DataKeySpec.AES_256;
import static software.amazon.awssdk.services.kms.model.MacAlgorithmSpec.HMAC_SHA_512;

/**
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html">AWS KMS Concepts</a>
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#data-keys">Data Keys</a>
 * @see <a href="https://aws.amazon.com/blogs/security/how-to-protect-hmacs-inside-aws-kms/">How to protect HMACs inside AWS KMS</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KmsProvider {

    private static final AwsCrypto AWS_ENCRYPTION_SDK =
            AwsCrypto.builder()
                    .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
                    .build();

    private final KmsClient kmsClient;
    private final KmsProperty kmsProperty;

    public byte[] requestJwtSignature() {
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

    public String encryptWithKeyRing(final String plainText) {
        IKeyring keyring = getAwsKmsKeyring(kmsProperty.encryptKeyArn());
        return encodeBase64String(AWS_ENCRYPTION_SDK.encryptData(keyring, plainText.getBytes(UTF_8)).getResult());
    }

    public String decryptWithKeyRing(final String encryptedText) {
        IKeyring keyring = getAwsKmsKeyring(kmsProperty.encryptKeyArn());
        return new String(AWS_ENCRYPTION_SDK.decryptData(keyring, decodeBase64String(encryptedText)).getResult());
    }

    private IKeyring getAwsKmsKeyring(String keyId) {
        final MaterialProviders materialProviders =
                MaterialProviders.builder()
                        .MaterialProvidersConfig(MaterialProvidersConfig.builder().build())
                        .build();

        final CreateAwsKmsMultiKeyringInput keyringInput =
                CreateAwsKmsMultiKeyringInput.builder().generator(keyId).build();
        return materialProviders.CreateAwsKmsMultiKeyring(keyringInput);
    }

    public DataKey makeDataKey() {
        GenerateDataKeyResponse response = requestGenerateDataKey();
        return new DataKey(response.ciphertextBlob().asByteArray(), response.plaintext().asByteArray());
    }

    private GenerateDataKeyResponse requestGenerateDataKey() {
        GenerateDataKeyRequest request = GenerateDataKeyRequest.builder()
                .keyId(kmsProperty.encryptKeyArn())
                .keySpec(AES_256)
                .build();

        GenerateDataKeyResponse response = kmsClient.generateDataKey(request);

        assert response.sdkHttpResponse().isSuccessful();
        return response;
    }

    public DecryptResponse requestPlaintextKey(byte[] encryptedKey) {
        DecryptRequest request = DecryptRequest.builder()
                .ciphertextBlob(SdkBytes.fromByteArray(encryptedKey))
                .build();

        DecryptResponse response = kmsClient.decrypt(request);

        assert response.sdkHttpResponse().isSuccessful();
        return response;
    }

}
