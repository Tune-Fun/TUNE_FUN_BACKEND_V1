package com.tune_fun.v1.interaction.application.port.output;

import com.tune_fun.v1.interaction.domain.RegisteredFollow;
import com.tune_fun.v1.interaction.domain.ScrollableFollowInfo;
import org.springframework.data.domain.Window;

import java.util.Optional;

public interface LoadFollowPort {

    Optional<RegisteredFollow> loadFollow(final Long followeeId, final Long followerId);

    Window<ScrollableFollowInfo> scrollFollow(final Long lastId, final Long followerId);

}
