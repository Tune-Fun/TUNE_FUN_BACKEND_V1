package com.tune_fun.v1.vote.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.hexagon.UseCase;
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

import java.util.List;

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
                loadNotificationApprovedDevice(true, null, null, null);
        log.info("notificationApprovedDevices: \n{}", objectUtil.objectToPrettyJson(notificationApprovedDevices));

        sendVotePaperRegisterNotification(votePaperRegisterEvent, notificationApprovedDevices);
    }

    @Retryable(retryFor = FirebaseMessagingException.class, recover = "recoverSendVotePaperRegisterFcm", backoff = @Backoff(delay = 2000, multiplier = 1.5, maxDelay = 10000))
    private void sendVotePaperRegisterNotification(final VotePaperRegisterEvent votePaperRegisterEvent, final List<NotificationApprovedDevice> notificationApprovedDevices) throws FirebaseMessagingException {
        if (RetrySynchronizationManager.getContext() != null) {
            RetryContext retryContext = RetrySynchronizationManager.getContext();
            log.info("Retry count: {}", retryContext.getRetryCount());

            log.error("sendVotePaperRegisterFcm FAILED. \n{}\nRetry count: {}", retryContext.getLastThrowable(), retryContext.getRetryCount());
        }

        SendVotePaperRegisterNotification sendVotePaperRegisterNotificationBehavior = voteBehaviorMapper
                .sendVotePaperRegisterFcm(votePaperRegisterEvent, notificationApprovedDevices);
        sendVoteNotificationPort.notification(sendVotePaperRegisterNotificationBehavior);
    }

    // TODO : Slack Notification?
    @Recover
    private void recoverSendVotePaperRegisterFcm(FirebaseMessagingException e, VotePaperRegisterEvent votePaperRegisterEvent, List<NotificationApprovedDevice> notificationApprovedDevices) {
        log.error("Failed to send FCM notification for vote paper register event: {}", votePaperRegisterEvent.id(), e);
    }
}
