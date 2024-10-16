package com.tune_fun.v1.external.aws.cloudwatch.logs;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.tune_fun.v1.external.aws.cloudwatch.logs.metrics.AwsLogsMetricsHolder;
import software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.tune_fun.v1.external.aws.cloudwatch.logs.AwsLogsAppender.MAX_BATCH_LOG_EVENTS;
import static java.lang.System.currentTimeMillis;

class AsyncWorker extends Worker implements Runnable {

    private final int maxBatchLogEvents;
    private final int discardThreshold;
    private final AtomicBoolean running;
    private final BlockingQueue<InputLogEvent> queue;
    private final AtomicLong lostCount;

    private Thread thread;

    AsyncWorker(AwsLogsAppender awsLogsAppender) {
        super(awsLogsAppender);
        maxBatchLogEvents = awsLogsAppender.getMaxBatchLogEvents();
        discardThreshold = (int) Math.ceil(maxBatchLogEvents * 1.5);
        running = new AtomicBoolean(false);
        queue = new ArrayBlockingQueue<InputLogEvent>(maxBatchLogEvents * 2);
        lostCount = new AtomicLong(0);
    }

    @Override
    public synchronized void start() {
        super.start();
        if (running.compareAndSet(false, true)) {
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.setName(getAwsLogsAppender().getName() + " Async Worker");
            thread.start();
        }
    }

    @Override
    public synchronized void stop() {
        if (running.compareAndSet(true, false)) {
            synchronized (running) {
                running.notifyAll();
            }
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    thread.interrupt();
                }
                thread = null;
            }
            queue.clear();
        }
        super.stop();
    }

    @Override
    public void append(ILoggingEvent event) {
        if (queue.size() >= discardThreshold && !event.getLevel().isGreaterOrEqual(Level.WARN)) {
            lostCount.incrementAndGet();
            AwsLogsMetricsHolder.get().incrementLostCount();
            synchronized (running) {
                running.notifyAll();
            }
            return;
        }
        InputLogEvent logEvent = asInputLogEvent(event);
        if (getAwsLogsAppender().getMaxBlockTimeMillis() > 0) {
            boolean interrupted = false;
            long until = currentTimeMillis() + getAwsLogsAppender().getMaxBlockTimeMillis();
            try {
                long now = currentTimeMillis();
                while (now < until) {
                    try {
                        if (!queue.offer(logEvent, until - now, TimeUnit.MILLISECONDS)) {
                            lostCount.incrementAndGet();
                            AwsLogsMetricsHolder.get().incrementLostCount();
                        }
                        break;
                    } catch (InterruptedException e) {
                        interrupted = true;
                        now = currentTimeMillis();
                    }
                }
            } finally {
                if (interrupted) Thread.currentThread().interrupt();
            }
        } else {
            if (!queue.offer(logEvent)) {
                lostCount.incrementAndGet();
                AwsLogsMetricsHolder.get().incrementLostCount();
            }
        }
        // trigger a flush if queue is full
        if (queue.size() >= maxBatchLogEvents) {
            synchronized (running) {
                running.notifyAll();
            }
        }
    }

    @Override
    public void run() {
        while (running.get()) {
            flush(false);
            try {
                synchronized (running) {
                    if (running.get()) {
                        running.wait(getAwsLogsAppender().getMaxFlushTimeMillis());
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
        flush(true);
    }

    private void flush(boolean all) {
        try {
            long lostCount = this.lostCount.getAndSet(0);
            if (lostCount > 0) {
                if (getAwsLogsAppender().getVerbose()) {
                    getAwsLogsAppender().addWarn(lostCount + " events lost");
                }
            }
            if (!queue.isEmpty()) {
                do {
                    Collection<InputLogEvent> batch = drainBatchFromQueue();
                    getAwsLogsAppender().getAwsLogsStub().logEvents(batch);
                } while (queue.size() >= maxBatchLogEvents || (all && !queue.isEmpty()));
            }
        } catch (Exception e) {
            AwsLogsMetricsHolder.get().incrementFlushFailed(e);
            if (getAwsLogsAppender().getVerbose()) {
                getAwsLogsAppender().addError("Unable to flush events to AWS", e);
            }
        }
    }
    
    private static final int MAX_BATCH_SIZE = 1048576;

    private Collection<InputLogEvent> drainBatchFromQueue() {
        Deque<InputLogEvent> batch = new ArrayDeque<InputLogEvent>(maxBatchLogEvents);
        queue.drainTo(batch, MAX_BATCH_LOG_EVENTS);
        int batchSize = batchSize(batch);
        while (batchSize > MAX_BATCH_SIZE) {
            InputLogEvent removed = batch.removeLast();
            batchSize -= eventSize(removed);
            if (!queue.offer(removed)) {
                AwsLogsMetricsHolder.get().incrementBatchRequeueFailed();
                if (getAwsLogsAppender().getVerbose())
                    getAwsLogsAppender().addWarn("Failed requeuing message from too big batch");
            }
        }

        AwsLogsMetricsHolder.get().incrementBatch(batchSize);
        return batch;
    }
    
    private static int batchSize(Collection<InputLogEvent> batch) {
        int size = 0;
        for (InputLogEvent event : batch) size += eventSize(event);
        return size;
    }
}
