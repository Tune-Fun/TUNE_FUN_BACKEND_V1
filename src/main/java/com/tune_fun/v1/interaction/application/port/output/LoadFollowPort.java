package com.tune_fun.v1.interaction.application.port.output;

import com.tune_fun.v1.interaction.domain.RegisteredFollow;

import java.util.Optional;

public interface LoadFollowPort {

    Optional<RegisteredFollow> loadFollow(final Long followeeId, final Long followerId);

}
