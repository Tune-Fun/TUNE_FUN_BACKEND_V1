package com.tune_fun.v1.external.aws.kms;

public record DataKey(byte[] encryptedKey, byte[] plainTextKey) {
}
