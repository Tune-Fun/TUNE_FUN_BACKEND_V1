package com.tune_fun.v1.external.aws.cloudwatch.logs;

import com.tune_fun.v1.common.config.aws.AwsCredentialFactory;
import com.tune_fun.v1.external.aws.cloudwatch.logs.metrics.AwsLogsMetricsHolder;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClientBuilder;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.nonNull;
import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;

class AWSLogsStub {
    private final Comparator<InputLogEvent> inputLogEventByTimestampComparator = Comparator.comparing(InputLogEvent::timestamp);
    private final String logGroupName;
    private final String logStreamName;
    private final String cloudWatchEndpoint;
    private final boolean verbose;
    private String sequenceToken;
    private Long lastTimestamp;
    private int retentionTimeInDays;

    private final Lazy<CloudWatchLogsClient> lazyAwsLogs = new Lazy<>();

    AWSLogsStub(String logGroupName, String logStreamName, int retentionTimeInDays
            , String cloudWatchEndpoint, boolean verbose) {
        this.logGroupName = logGroupName;
        this.logStreamName = logStreamName;
        this.retentionTimeInDays = retentionTimeInDays;
        this.cloudWatchEndpoint = cloudWatchEndpoint;
        this.verbose = verbose;
    }

    private CloudWatchLogsClient awsLogs() {
        return lazyAwsLogs.getOrCompute(() -> {
            if (verbose) System.out.println("Creating AWSLogs Client");

            CloudWatchLogsClientBuilder builder = CloudWatchLogsClient.builder();

            if (nonNull(cloudWatchEndpoint)) {
                try {
                    builder = builder.endpointOverride(new URI(cloudWatchEndpoint));
                } catch (URISyntaxException e) {

                    if (verbose) System.out.println("Invalid endpoint endpoint URL: " + cloudWatchEndpoint);
                }
            }

            CloudWatchLogsClient awsLogs = builder
                    .region(AP_NORTHEAST_2)
                    .credentialsProvider(AwsCredentialFactory.ec2Instance())
                    .build();

            initLogGroup(awsLogs);
            return awsLogs;
        });
    }

    private void initLogGroup(CloudWatchLogsClient awsLogs) {
        try {
            awsLogs.createLogGroup(CreateLogGroupRequest.builder()
                    .logGroupName(logGroupName)
                    .build());
            if (retentionTimeInDays > 0) {
                awsLogs.putRetentionPolicy(PutRetentionPolicyRequest.builder()
                        .logGroupName(logGroupName)
                        .retentionInDays(retentionTimeInDays)
                        .build());
            }
        } catch (Throwable ignored) {
        }
        try {
            awsLogs.createLogStream(CreateLogStreamRequest.builder()
                    .logGroupName(logGroupName)
                    .logStreamName(logStreamName)
                    .build());
        } catch (Throwable ignored) {
        }
    }

    synchronized void start() {
    }

    synchronized void stop() {
        try {
            awsLogs().close();
        } catch (Exception e) {
            // ignore
        }
    }

    synchronized void logEvents(Collection<InputLogEvent> events) {
        if (events.size() > 1) {
            List<InputLogEvent> sortedEvents = new ArrayList<>(events);
            sortedEvents.sort(inputLogEventByTimestampComparator);
            events = sortedEvents;
        }

        List<InputLogEvent> correctedEvents = new ArrayList<>(events.size());
        for (InputLogEvent event : events) {
            if (lastTimestamp != null && event.timestamp() < lastTimestamp) {
                correctedEvents.add(event.toBuilder()
                        .timestamp(lastTimestamp)
                        .build());
            } else {
                correctedEvents.add(event);
                lastTimestamp = event.timestamp();
            }
        }
        AwsLogsMetricsHolder.get().incrementLogEvents(correctedEvents.size());
        AwsLogsMetricsHolder.get().incrementPutLog();
        logPreparedEvents(correctedEvents);
    }

    private void logPreparedEvents(Collection<InputLogEvent> events) {
        try {
            PutLogEventsRequest request = PutLogEventsRequest.builder()
                    .logGroupName(logGroupName)
                    .logStreamName(logStreamName)
                    .sequenceToken(sequenceToken)
                    .logEvents(events)
                    .build();
            PutLogEventsResponse result = awsLogs().putLogEvents(request);
            sequenceToken = result.nextSequenceToken();
        } catch (DataAlreadyAcceptedException e) {
            sequenceToken = e.expectedSequenceToken();
        } catch (InvalidSequenceTokenException e) {
            sequenceToken = e.expectedSequenceToken();
            logPreparedEvents(events);
        } catch (Throwable t) {
            AwsLogsMetricsHolder.get().incrementPutLogFailed(t);
            throw t;
        }
    }
}
