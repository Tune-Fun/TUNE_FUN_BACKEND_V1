package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.account.adapter.output.persistence.QAccountJpaEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DeviceCustomRepositoryImpl implements DeviceCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final QDeviceJpaEntity DEVICE = QDeviceJpaEntity.deviceJpaEntity;
    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;

    @Override
    public Optional<DeviceJpaEntity> findByFcmTokenOrDeviceToken(String username, String fcmToken, String deviceToken) {
        return Optional.ofNullable(
                queryFactory.selectFrom(DEVICE)
                        .join(DEVICE.account, ACCOUNT).fetchJoin()
                        .where(
                                ACCOUNT.username.eq(username),
                                DEVICE.fcmToken.eq(fcmToken).or(DEVICE.deviceToken.eq(deviceToken))
                        )
                        .fetchOne()
        );
    }
}
