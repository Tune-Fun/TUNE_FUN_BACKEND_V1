package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public abstract class VoteBehaviorMapper {

    public abstract SaveVotePaper saveVotePaper(final VotePaperCommands.Register command);
}
