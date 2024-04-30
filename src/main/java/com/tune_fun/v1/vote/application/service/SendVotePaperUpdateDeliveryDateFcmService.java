package com.tune_fun.v1.vote.application.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperUpdateDeliveryDateFcmUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePort;
import com.tune_fun.v1.vote.application.port.output.SendVoteFcmPort;
import com.tune_fun.v1.vote.domain.behavior.SendVotePaperUpdateDeliveryDateFcm;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateDeliveryDateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@UseCase
@RequiredArgsConstructor
public class SendVotePaperUpdateDeliveryDateFcmService implements SendVotePaperUpdateDeliveryDateFcmUseCase {

    private final LoadVotePort loadVotePort;
    private final LoadDevicePort loadDevicePort;
    private final SendVoteFcmPort sendVoteFcmPort;

    private final VoteBehaviorMapper voteBehaviorMapper;


    @Override
    public void send(final VotePaperUpdateDeliveryDateEvent event) throws FirebaseMessagingException {
        List<Long> voterIds = loadVotePort.loadVoterIdsByVotePaperUuid(event.id());

        List<NotificationApprovedDevice> notificationApprovedDevices =
                loadDevicePort.loadNotificationApprovedDevice(null, null, true);

        SendVotePaperUpdateDeliveryDateFcm sendVotePaperUpdateDeliveryDateFcmBehavior =
                voteBehaviorMapper.sendVotePaperUpdateDeliveryDateFcm(event, notificationApprovedDevices);

        sendVoteFcmPort.notification(sendVotePaperUpdateDeliveryDateFcmBehavior);
    }


}
