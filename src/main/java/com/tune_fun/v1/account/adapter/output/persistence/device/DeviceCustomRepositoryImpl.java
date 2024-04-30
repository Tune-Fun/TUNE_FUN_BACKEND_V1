package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.account.adapter.output.persistence.QAccountJpaEntity;
import com.tune_fun.v1.account.domain.value.NotificationApprovedDevice;
import com.tune_fun.v1.common.util.querydsl.PredicateBuilder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Projections.fields;

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

    @Override
    public List<NotificationApprovedDevice> fetchNotificationApprovedDevice(
            final Boolean voteProgressNotification, final Boolean voteEndNotification, final Boolean voteDeliveryNotification,
            final List<Long> accountIds) {

        Predicate predicate = PredicateBuilder.builder()
                .and().eqBoolean(ACCOUNT.notificationConfig.voteProgressNotification, voteProgressNotification)
                .and().eqBoolean(ACCOUNT.notificationConfig.voteEndNotification, voteEndNotification)
                .and().eqBoolean(ACCOUNT.notificationConfig.voteDeliveryNotification, voteDeliveryNotification)
                .and().inNumber(ACCOUNT.id, accountIds)
                .build();

        return queryFactory.select(fields(NotificationApprovedDevice.class,
                                DEVICE.id.as("id"),
                                ACCOUNT.username,
                                ACCOUNT.nickname,
                                DEVICE.fcmToken,
                                DEVICE.deviceToken
                        )
                )
                .from(DEVICE)
                .join(DEVICE.account, ACCOUNT)
                .where(predicate)
                .fetch();
    }
}
