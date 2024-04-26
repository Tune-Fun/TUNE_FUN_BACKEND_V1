package com.tune_fun.v1.account.application.port.output.device;

import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;

import java.util.List;

public interface LoadDevicePort {

    List<NotificationApprovedDevice> loadNotificationApprovedDevice(
            Boolean voteProgressNotification, Boolean voteEndNotification, Boolean voteDeliveryNotification);
}
