package com.tune_fun.v1.common.util.count;

import java.util.concurrent.atomic.LongAdder;

public final class AtomicCounter implements Counter {

    private final LongAdder concreteCounter = new LongAdder();

    @Override
    public void increment() {
        concreteCounter.increment();
    }

    @Override
    public void decrement() {
        concreteCounter.decrement();
    }

    @Override
    public long get() {
        return concreteCounter.sum();
    }
}
