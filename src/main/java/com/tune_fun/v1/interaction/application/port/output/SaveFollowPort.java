package com.tune_fun.v1.interaction.application.port.output;

public interface SaveFollowPort {
    void saveFollow(final Long followeeId, final Long followerId);
}
