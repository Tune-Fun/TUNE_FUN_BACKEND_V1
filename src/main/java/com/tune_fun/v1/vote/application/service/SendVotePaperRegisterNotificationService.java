package com.tune_fun.v1.vote.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.common.util.ObjectUtil;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperRegisterNotificationUseCase;
import com.tune_fun.v1.vote.application.port.output.SendVoteNotificationPort;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperRegisterNotification;
import com.tune_fun.v1.vote.domain.event.VotePaperRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.tune_fun.v1.common.constant.Constants.NULL_BOOLEAN;

@Slf4j
@Service
@UseCase
@RequiredArgsConstructor
public class SendVotePaperRegisterNotificationService implements SendVotePaperRegisterNotificationUseCase {

    private final LoadDevicePort loadDevicePort;
    private final SendVoteNotificationPort sendVoteNotificationPort;

    private final VoteBehaviorMapper voteBehaviorMapper;

    private final ObjectUtil objectUtil;

    @Transactional
    @Override
    public void send(VotePaperRegisterEvent votePaperRegisterEvent) throws JsonProcessingException, FirebaseMessagingException {
        // TODO : Follower Aggregate Root................... -> accountIds

        List<NotificationApprovedDevice> notificationApprovedDevices = loadDevicePort.
                loadNotificationApprovedDevice(true, NULL_BOOLEAN, NULL_BOOLEAN, new ArrayList<>());
        log.info("notificationApprovedDevices: \n{}", objectUtil.objectToPrettyJson(notificationApprovedDevices));

        if (!notificationApprovedDevices.isEmpty())
            sendVotePaperRegisterNotification(votePaperRegisterEvent, notificationApprovedDevices);
    }

    @Retryable(retryFor = FirebaseMessagingException.class, recover = "recoverSendVotePaperRegisterNotification", backoff = @Backoff(delay = 2000, multiplier = 1.5, maxDelay = 10000))
    private void sendVotePaperRegisterNotification(final VotePaperRegisterEvent event, final List<NotificationApprovedDevice> devices) throws FirebaseMessagingException {
        if (RetrySynchronizationManager.getContext() != null) {
            RetryContext retryContext = RetrySynchronizationManager.getContext();
            log.info("Retry count: {}", retryContext.getRetryCount());

            log.error("sendVotePaperRegisterNotification FAILED. \n{}\nRetry count: {}", retryContext.getLastThrowable(), retryContext.getRetryCount());
        }

        SendVotePaperRegisterNotification sendVotePaperRegisterNotificationBehavior = voteBehaviorMapper
                .sendVotePaperRegisterNotification(event, devices);
        sendVoteNotificationPort.notification(sendVotePaperRegisterNotificationBehavior);
    }

    // TODO : Slack Notification?
    @Recover
    private void recoverSendVotePaperRegisterNotification(FirebaseMessagingException e, VotePaperRegisterEvent event, List<NotificationApprovedDevice> notificationApprovedDevices) {
        log.error("Failed to send FCM notification for vote paper register event: {}", event.id(), e);
    }
}
