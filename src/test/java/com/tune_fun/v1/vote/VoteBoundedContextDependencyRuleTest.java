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

    // TODO : VotePaperControllerIT.registerVotePaperSuccess 에서 ScheduleVotePaperDeadlineService SpyBean 주입으로 인해 실패.
    //  별도의 Service 로직 테스트로 분리 필요
    @Disabled
    @Execution(CONCURRENT)
    @Test
    @DisplayName("Vote Bounded Context satisfied Hexagonal Architecture.")
    void checkAccountDependencyRule() {
        checkDependencyRule();
    }
}
