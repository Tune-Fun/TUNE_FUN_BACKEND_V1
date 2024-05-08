package com.tune_fun.v1.vote.application.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.common.response.MessageCode;
import com.tune_fun.v1.vote.application.port.input.usecase.ScheduleVotePaperDeadlineUseCase;
import com.tune_fun.v1.vote.application.port.output.SendVoteNotificationPort;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperEndNotification;
import com.tune_fun.v1.vote.domain.event.VotePaperDeadlineEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tune_fun.v1.common.constant.Constants.NULL_BOOLEAN;

@Slf4j
@Service
@UseCase
@RequiredArgsConstructor
public class ScheduleVotePaperDeadlineService implements ScheduleVotePaperDeadlineUseCase {

    private final LoadDevicePort loadDevicePort;
    private final SendVoteNotificationPort sendVoteNotificationPort;

    private final Clock clock;
    private final SimpleAsyncTaskScheduler taskScheduler;

    private final VoteBehaviorMapper voteBehaviorMapper;

    @Override
    public void scheduleVotePaperDeadlineAction(VotePaperDeadlineEvent event) {
        final LocalDateTime deadline = event.voteEndAt();
        final Instant deadlineInstant = toInstant(deadline);

        taskScheduler.schedule(() -> {
            try {
                sendVotePaperEndNotification(event);
            } catch (FirebaseMessagingException e) {
                throw new CommonApplicationException(MessageCode.ERROR);
            }
        }, deadlineInstant);

    }

    public void sendVotePaperEndNotification(VotePaperDeadlineEvent event) throws FirebaseMessagingException {
        List<NotificationApprovedDevice> devices = loadDevicePort.
                loadNotificationApprovedDevice(NULL_BOOLEAN, true, NULL_BOOLEAN, new ArrayList<>());

        if (!devices.isEmpty()) {
            SendVotePaperEndNotification behavior = voteBehaviorMapper.sendVotePaperEndNotification(event, devices);
            sendVoteNotificationPort.notification(behavior);
        }
    }

    private Instant toInstant(final LocalDateTime localDateTime) {
        return localDateTime.atZone(clock.getZone()).toInstant();
    }
}
