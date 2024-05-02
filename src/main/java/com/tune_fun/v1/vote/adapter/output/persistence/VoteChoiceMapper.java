package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.common.constant.Constants;
import com.tune_fun.v1.common.util.StringUtil;
import com.tune_fun.v1.vote.domain.behavior.SaveVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(
        config = BaseMapperConfig.class,
        imports = {StringUtil.class, Constants.class}
)
public abstract class VoteChoiceMapper {

    @IterableMapping(qualifiedByName = "registeredVoteChoice")
    public abstract List<RegisteredVoteChoice> registeredVoteChoices(final List<VoteChoiceJpaEntity> voteChoiceJpaEntities);

    @Named("registeredVoteChoice")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "votePaperId", source = "votePaper.id")
    @Mapping(target = "music", source = "offer.music")
    @Mapping(target = "artistName", source = "offer.artistName")
    @Mapping(target = "genres", source = "offer.genres", qualifiedByName = "splitString")
    @Mapping(target = "releaseDate", source = "offer.releaseDate")
    @Mapping(target = "durationMs", source = "offer.durationMs")
    public abstract RegisteredVoteChoice registeredVoteChoice(final VoteChoiceJpaEntity voteChoiceJpaEntity);

    @Mapping(target = "votePaper", source = "votePaperJpaEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract VoteChoiceJpaEntity.VoteChoiceJpaEntityBuilder<?, ?> updateVoteChoice(VotePaperJpaEntity votePaperJpaEntity,
                                                                                          @MappingTarget final VoteChoiceJpaEntity.VoteChoiceJpaEntityBuilder<?, ?> voteChoiceJpaEntityBuilder);

    @IterableMapping(qualifiedByName = "fromSaveVoteChoiceBehavior")
    public abstract Set<VoteChoiceJpaEntity> fromSaveVoteChoiceBehaviors(final Set<SaveVoteChoice> behavior);

    @Named("fromSaveVoteChoiceBehavior")
    @Mapping(target = "uuid", expression = "java(StringUtil.uuid())")
    @Mapping(target = "offer", source = ".", qualifiedByName = "voteChoiceOffer")
    public abstract VoteChoiceJpaEntity fromSaveVoteChoiceBehavior(final SaveVoteChoice behavior);

    @Named("voteChoiceOffer")
    @Mapping(target = "music", source = "music")
    @Mapping(target = "artistName", source = "offerArtistName")
    @Mapping(target = "genres", source = "offerGenres", qualifiedByName = "joinString")
    @Mapping(target = "releaseDate", source = "offerReleaseDate")
    @Mapping(target = "durationMs", source = "offerDurationMs")
    public abstract Offer voteChoiceOffer(final SaveVoteChoice behavior);

    @Named("joinString")
    public String joinString(final Set<String> strings) {
        return String.join(Constants.COMMA, strings);
    }

    @Named("splitString")
    public Set<String> splitString(final String string) {
        return Set.of(string.split(Constants.COMMA));
    }
}
