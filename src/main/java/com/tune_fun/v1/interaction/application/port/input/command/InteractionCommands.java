package com.tune_fun.v1.interaction.application.port.input.command;

public class InteractionCommands {

    public record Follow(Long targetAccountId) {

    }

    public record UnFollow(Long targetAccountId) {

    }

}
