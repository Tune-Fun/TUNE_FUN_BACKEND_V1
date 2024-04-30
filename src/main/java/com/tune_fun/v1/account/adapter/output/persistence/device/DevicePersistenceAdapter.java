package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.application.port.output.device.DeleteDevicePort;
import com.tune_fun.v1.account.application.port.output.device.LoadDevicePort;
import com.tune_fun.v1.account.application.port.output.device.SaveDevicePort;
import com.tune_fun.v1.account.domain.behavior.DeleteDevice;
import com.tune_fun.v1.account.domain.behavior.SaveDevice;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import com.tune_fun.v1.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class DevicePersistenceAdapter implements SaveDevicePort, LoadDevicePort, DeleteDevicePort {

    private final AccountPersistenceAdapter accountPersistenceAdapter;
    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    @Override
    public void saveDevice(final SaveDevice behavior) {
        Optional<DeviceJpaEntity> byDeviceToken = deviceRepository.findByDeviceToken(behavior.deviceToken());

        if (byDeviceToken.isPresent()) {
            DeviceJpaEntity updated = deviceMapper.updateDeviceJpaEntity(behavior, byDeviceToken.get().toBuilder());
            deviceRepository.save(updated);
            return;
        }

        accountPersistenceAdapter.loadAccountByUsername(behavior.username())
                .ifPresent(account -> {
                    DeviceJpaEntity deviceJpaEntity = DeviceJpaEntity.builder()
                            .account(account)
                            .uuid(StringUtil.uuid())
                            .fcmToken(behavior.fcmToken())
                            .deviceToken(behavior.deviceToken())
                            .build();
                    deviceRepository.save(deviceJpaEntity);
                });
    }

    @Override
    public List<NotificationApprovedDevice> loadNotificationApprovedDevice(
            final Boolean voteProgressNotification, final Boolean voteEndNotification, final Boolean voteDeliveryNotification) {
        return deviceRepository.fetchNotificationApprovedDevice(voteProgressNotification, voteEndNotification,
                voteDeliveryNotification);
    }

    @Override
    public void delete(final DeleteDevice behavior) {
        findByFcmTokenOrDeviceToken(behavior.username(), behavior.fcmToken(), behavior.deviceToken()).ifPresent(deviceRepository::delete);
    }

    public Optional<DeviceJpaEntity> findByFcmTokenOrDeviceToken(String username, String fcmToken, String deviceToken) {
        return deviceRepository
                .findByFcmTokenOrDeviceToken(username, fcmToken, deviceToken);
    }
}
