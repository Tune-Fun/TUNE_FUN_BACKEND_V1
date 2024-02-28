package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.tune_fun.v1.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.tune_fun.v1.account.application.port.output.device.SaveDevicePort;
import com.tune_fun.v1.account.domain.behavior.SaveDevice;
import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.tune_fun.v1.common.util.StringUtil.uuid;

@XRayEnabled
@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class DevicePersistenceAdapter implements SaveDevicePort {

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
                            .uuid(uuid())
                            .fcmToken(behavior.fcmToken())
                            .deviceToken(behavior.deviceToken())
                            .build();
                    deviceRepository.save(deviceJpaEntity);
                });
    }
}
