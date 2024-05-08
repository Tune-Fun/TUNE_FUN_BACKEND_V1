package com.tune_fun.v1.vote.application.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperUpdateDeliveryDateNotificationUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePort;
import com.tune_fun.v1.vote.application.port.output.SendVoteNotificationPort;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateDeliveryDateNotification;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tune_fun.v1.common.constant.Constants.NULL_BOOLEAN;

@Slf4j
@Service
@UseCase
@RequiredArgsConstructor
public class SendVotePaperUpdateDeliveryDateNotificationService implements SendVotePaperUpdateDeliveryDateNotificationUseCase {

    private final LoadVotePort loadVotePort;
    private final LoadDevicePort loadDevicePort;

    private final SendVoteNotificationPort sendVoteNotificationPort;

    private final VoteBehaviorMapper voteBehaviorMapper;


    @Override
    public void send(final VotePaperUpdateDeliveryDateEvent event) throws FirebaseMessagingException {
        List<Long> voterIds = loadVotePort.loadVoterIdsByVotePaperUuid(event.id());

        List<NotificationApprovedDevice> notificationApprovedDevices =
                loadDevicePort.loadNotificationApprovedDevice(NULL_BOOLEAN, NULL_BOOLEAN, true, voterIds);

        if (!notificationApprovedDevices.isEmpty())
            sendVotePaperUpdateDeliveryDateNotification(event, notificationApprovedDevices);
    }

    @Retryable(retryFor = FirebaseMessagingException.class, recover = "recoverSendVotePaperUpdateDeliveryDateNotification", backoff = @Backoff(delay = 2000, multiplier = 1.5, maxDelay = 10000))
    private void sendVotePaperUpdateDeliveryDateNotification(final VotePaperUpdateDeliveryDateEvent event, final List<NotificationApprovedDevice> devices) throws FirebaseMessagingException {
        if (RetrySynchronizationManager.getContext() != null) {
            RetryContext retryContext = RetrySynchronizationManager.getContext();
            log.info("Retry count: {}", retryContext.getRetryCount());

            log.error("sendVotePaperUpdateDeliveryDateNotification FAILED. \n{}\nRetry count: {}", retryContext.getLastThrowable(), retryContext.getRetryCount());
        }


        SendVotePaperUpdateDeliveryDateNotification behavior = voteBehaviorMapper
                .sendVotePaperUpdateDeliveryDateNotification(event, devices);
        sendVoteNotificationPort.notification(behavior);
    }

    // TODO : Slack Notification?
    @Recover
    private void recoverSendVotePaperUpdateDeliveryDateNotification(final FirebaseMessagingException e, final VotePaperUpdateDeliveryDateEvent event, final List<NotificationApprovedDevice> devices) {
        log.error("sendVotePaperUpdateDeliveryDateNotification FAILED. \n{}", event.id(), e);
    }


}
