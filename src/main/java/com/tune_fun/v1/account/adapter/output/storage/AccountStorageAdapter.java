package com.tune_fun.v1.account.adapter.output.storage;

import com.tune_fun.v1.external.aws.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountStorageAdapter {

    private final S3Template s3Template;

}
