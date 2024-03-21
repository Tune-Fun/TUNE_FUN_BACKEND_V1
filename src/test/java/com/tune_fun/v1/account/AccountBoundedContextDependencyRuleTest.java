package com.tune_fun.v1.account;

import com.tune_fun.v1.base.architecture.BoundedContextDependencyRuleTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

public class AccountBoundedContextDependencyRuleTest extends BoundedContextDependencyRuleTest {

    private static final String BOUNDED_CONTEXT_PACKAGE = "account";

    @Override
    public String getBoundedContextPackage() {
        return BOUNDED_CONTEXT_PACKAGE;
    }


    // TODO : OAuth2 도메인 의존성 수정 필요
    @Disabled
    @Execution(CONCURRENT)
    @Test
    @DisplayName("Account Bounded Context satisfied Hexagonal Architecture.")
    void checkAccountDependencyRule() {
        checkDependencyRule();
    }

}
