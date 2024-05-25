package com.tune_fun.v1.common.util.count;

public sealed interface Counter permits AtomicCounter {
    void increment();

    void decrement();

    long get();
}
