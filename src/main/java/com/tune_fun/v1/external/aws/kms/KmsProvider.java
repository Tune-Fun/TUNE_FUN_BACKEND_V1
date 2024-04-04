package com.tune_fun.v1.external.aws.kms;

import com.tune_fun.v1.common.property.KmsProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

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
