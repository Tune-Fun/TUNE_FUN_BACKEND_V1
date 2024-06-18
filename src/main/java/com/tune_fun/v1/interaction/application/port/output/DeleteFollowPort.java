package com.tune_fun.v1.interaction.application.port.output;

public interface DeleteFollowPort {
    void deleteFollow(final Long followeeId, final Long followerId);
}
