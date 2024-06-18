package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.interaction.domain.RegisteredFollow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public abstract class FollowMapper {

    public abstract RegisteredFollow registeredFollow(final FollowJpaEntity entity);

    @Mapping(target = "followerId", source = "followerId")
    @Mapping(target = "followeeId", source = "followeeId")
    public abstract FollowJpaEntity follow(final Long followeeId, final Long followerId);

}
