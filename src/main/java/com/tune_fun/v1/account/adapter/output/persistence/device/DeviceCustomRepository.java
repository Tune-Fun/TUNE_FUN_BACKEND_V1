package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;

import java.util.List;
import java.util.Optional;

public interface DeviceCustomRepository {
    Optional<DeviceJpaEntity> findByFcmTokenOrDeviceToken(final String username, final String fcmToken, final String deviceToken);

    List<NotificationApprovedDevice> fetchNotificationApprovedDevice(
            final Boolean voteProgressNotification, final Boolean voteEndNotification, final Boolean voteDeliveryNotification, final List<Long> accountIds);
}
