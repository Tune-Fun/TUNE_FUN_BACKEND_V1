package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.behavior.SaveVotePaper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(config = BaseMapperConfig.class)
public abstract class VoteBehaviorMapper {

    public abstract SaveVotePaper saveVotePaper(final VotePaperCommands.Register command);

    @IterableMapping(qualifiedByName = "saveVoteChoice")
    public abstract Set<SaveVoteChoice> saveVoteChoices(final Set<VotePaperCommands.Offer> offers);

    @Named("saveVoteChoice")
    @Mapping(target = "offerArtistName", source = "artistName")
    @Mapping(target = "offerGenres", source = "genres")
    @Mapping(target = "offerDurationMs", source = "durationMs")
    @Mapping(target = "offerReleaseDate", source = "releaseDate")
    public abstract SaveVoteChoice saveVoteChoice(final VotePaperCommands.Offer offer);
}
