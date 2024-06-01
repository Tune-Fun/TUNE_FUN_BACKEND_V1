package com.tune_fun.v1.account.application.port.output;

public interface RecordEmailVerifiedAtPort {
    void recordEmailVerifiedAt(final String username);

    void clearEmailVerifiedAt(final String username);
}
