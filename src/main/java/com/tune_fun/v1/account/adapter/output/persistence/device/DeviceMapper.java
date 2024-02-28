package com.tune_fun.v1.account.adapter.output.persistence.device;

import com.tune_fun.v1.account.domain.behavior.SaveDevice;
import com.tune_fun.v1.common.config.BaseMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = BaseMapperConfig.class)
public abstract class DeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "fcmToken", source = "fcmToken")
    @Mapping(target = "deviceToken", source = "deviceToken")
    public abstract DeviceJpaEntity updateDeviceJpaEntity(final SaveDevice saveDevice,
                                                          @MappingTarget DeviceJpaEntity.DeviceJpaEntityBuilder<?, ?> deviceJpaEntityBuilder);

}
