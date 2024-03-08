package com.tune_fun.v1.account.adapter.output.persistence.device;

import java.util.Optional;

public interface DeviceCustomRepository {
    Optional<DeviceJpaEntity> findByFcmTokenOrDeviceToken(final String username, final String fcmToken, final String deviceToken);
}
