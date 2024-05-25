package com.tune_fun.v1.common.util.count;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.atomic.LongAdder;

public final class AtomicCounter implements Counter, Serializable {

    @Serial
    private static final long serialVersionUID = 119874651351L;

    private final LongAdder concreteCounter = new LongAdder();

    public void increment(long i) {
        concreteCounter.add(i);
    }

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
