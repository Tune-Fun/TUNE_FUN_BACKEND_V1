package com.tune_fun.v1.account.adapter.output.persistence.device;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceJpaEntity, Long>, DeviceCustomRepository {
    Optional<DeviceJpaEntity> findByDeviceToken(final String deviceToken);
}