package com.tune_fun.v1.common.rate;

import io.github.bucket4j.Bandwidth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

@Getter
@RequiredArgsConstructor
public enum RatePlan {

    TEST("test") {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.builder()
                    .capacity(100)
                    .refillIntervally(10, ofSeconds(1))
                    .build();
        }
    },
    LOCAL("local") {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.builder()
                    .capacity(5)
                    .refillIntervally(1, ofMinutes(1))
                    .build();
        }
    };

    private final String planName;

    public static Bandwidth resolvePlan(String targetPlan) {
        for (RatePlan plan : RatePlan.values()) {
            if (plan.getPlanName().equals(targetPlan)) {
                return plan.getLimit();
            }
        }
        throw new RuntimeException("RatePlan not found");
    }

    public abstract Bandwidth getLimit();

}
