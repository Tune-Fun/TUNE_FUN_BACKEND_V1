package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.interaction.domain.RegisteredFollow;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public abstract class FollowMapper {

    public abstract RegisteredFollow registeredFollow(final FollowJpaEntity entity);

    public abstract FollowJpaEntity follow(final Long followeeId, final Long followerId);

}
