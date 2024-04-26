package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.hexagon.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.SendVotePaperRegisterFcmUseCase;
import com.tune_fun.v1.vote.application.port.output.SendVoteFcmPort;
import com.tune_fun.v1.vote.domain.behavior.ProduceVotePaperUploadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@UseCase
@RequiredArgsConstructor
public class SendVotePaperRegisterFcmService implements SendVotePaperRegisterFcmUseCase {

    private final LoadDevicePort loadDevicePort;

    private final SendVoteFcmPort sendVoteFcmPort;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void send(ProduceVotePaperUploadEvent produceVotePaperUploadEvent) {
        List<NotificationApprovedDevice> notificationApprovedDevices = loadDevicePort.
                loadNotificationApprovedDevice(true, null, null);

        // TODO : FCM 발송 로직 구현
    }
}
