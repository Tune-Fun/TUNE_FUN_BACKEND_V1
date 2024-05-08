package com.tune_fun.v1.vote.application.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperUpdateVideoUrlNotificationUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePort;
import com.tune_fun.v1.vote.application.port.output.SendVoteNotificationPort;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateVideoUrlNotification;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;
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
public class SendVotePaperUpdateVideoUrlNotificationService implements SendVotePaperUpdateVideoUrlNotificationUseCase {

    private final LoadVotePort loadVotePort;
    private final LoadDevicePort loadDevicePort;

    private final SendVoteNotificationPort sendVoteNotificationPort;

    private final VoteBehaviorMapper voteBehaviorMapper;

    @Override
    public void send(final VotePaperUpdateVideoUrlEvent event) throws FirebaseMessagingException {
        List<Long> voterIds = loadVotePort.loadVoterIdsByVotePaperUuid(event.id());

        List<NotificationApprovedDevice> notificationApprovedDevices =
                loadDevicePort.loadNotificationApprovedDevice(NULL_BOOLEAN, NULL_BOOLEAN, true, voterIds);

        if (!notificationApprovedDevices.isEmpty())
            sendVotePaperUpdateVideoUrlNotification(event, notificationApprovedDevices);
    }

    @Retryable(retryFor = FirebaseMessagingException.class, recover = "recoverSendVotePaperUpdateVideoUrlNotification", backoff = @Backoff(delay = 2000, multiplier = 1.5, maxDelay = 10000))
    private void sendVotePaperUpdateVideoUrlNotification(final VotePaperUpdateVideoUrlEvent event, final List<NotificationApprovedDevice> devices) throws FirebaseMessagingException {
        if (RetrySynchronizationManager.getContext() != null) {
            RetryContext retryContext = RetrySynchronizationManager.getContext();
            log.info("Retry count: {}", retryContext.getRetryCount());

            log.error("sendVotePaperUpdateDeliveryDateNotification FAILED. \n{}\nRetry count: {}", retryContext.getLastThrowable(), retryContext.getRetryCount());
        }

        SendVotePaperUpdateVideoUrlNotification behavior = voteBehaviorMapper.sendVotePaperUpdateVideoUrlNotification(event, devices);
        sendVoteNotificationPort.notification(behavior);
    }

    // TODO : Slack Notification?
    @Recover
    private void recoverSendVotePaperUpdateVideoUrlNotification(final FirebaseMessagingException e, final VotePaperUpdateVideoUrlEvent event, final List<NotificationApprovedDevice> devices) {
        log.error("recoverSendVotePaperUpdateVideoUrlNotification FAILED. \n{}", event.id(), e);
    }

}
