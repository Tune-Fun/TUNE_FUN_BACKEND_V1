package com.tune_fun.v1.otp;

import com.tune_fun.v1.base.architecture.BoundedContextDependencyRuleTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OtpBoundedContextDependencyRuleTest extends BoundedContextDependencyRuleTest {

    private static final String ROOT_PACKAGE = "com.tune_fun.v1.otp";

    @Override
    public String getRootPackage() {
        return ROOT_PACKAGE;
    }

    @Disabled
    @Test
    @DisplayName("Otp Bounded Context satisfied Hexagonal Architecture.")
    void checkOtpDependencyRule() {
        checkDependencyRule();
    }
}
