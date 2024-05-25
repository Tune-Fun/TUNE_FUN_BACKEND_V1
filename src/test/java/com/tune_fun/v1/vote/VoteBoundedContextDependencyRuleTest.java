package com.tune_fun.v1.vote;

import com.tune_fun.v1.base.architecture.BoundedContextDependencyRuleTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

public class VoteBoundedContextDependencyRuleTest extends BoundedContextDependencyRuleTest {

    private static final String BOUNDED_CONTEXT_PACKAGE = "vote";

    @Override
    public String getBoundedContextPackage() {
        return BOUNDED_CONTEXT_PACKAGE;
    }

    @Execution(CONCURRENT)
    @Test
    @DisplayName("Vote Bounded Context satisfied Hexagonal Architecture.")
    void checkAccountDependencyRule() {
        checkDependencyRule();
    }
}
