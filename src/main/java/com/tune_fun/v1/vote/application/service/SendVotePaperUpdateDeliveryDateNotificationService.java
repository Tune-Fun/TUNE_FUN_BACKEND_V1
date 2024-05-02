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
import org.springframework.stereotype.Service;

import java.util.List;

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
                loadDevicePort.loadNotificationApprovedDevice(null, null, true, voterIds);

        SendVotePaperUpdateDeliveryDateNotification behavior = voteBehaviorMapper.sendVotePaperUpdateDeliveryDateNotification(event, notificationApprovedDevices);
        sendVoteNotificationPort.notification(behavior);
    }


}
