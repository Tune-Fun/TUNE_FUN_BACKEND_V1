package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.config.BaseMapperConfig;
import com.tune_fun.v1.vote.domain.value.FullVotePaper;
import com.tune_fun.v1.vote.domain.value.RegisteredVoteChoice;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import com.tune_fun.v1.vote.domain.value.VotePaperStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = BaseMapperConfig.class)
public abstract class VoteValueMapper {

    @Mapping(target = "id", source = "votePaper.id")
    @Mapping(target = "uuid", source = "votePaper.uuid")
    @Mapping(target = "author", source = "votePaper.author")
    @Mapping(target = "authorUsername", source = "votePaper.authorUsername")
    @Mapping(target = "profileImageUrl", source = "votePaper.profileImageUrl")
    @Mapping(target = "title", source = "votePaper.title")
    @Mapping(target = "content", source = "votePaper.content")
    @Mapping(target = "totalVoteCount", source = "votePaperStatistics.voteCount")
    @Mapping(target = "totalLikeCount", source = "votePaperStatistics.likeCount")
    @Mapping(target = "option", source = "votePaper.option")
    @Mapping(target = "videoUrl", source = "votePaper.videoUrl")
    @Mapping(target = "isVoted", source = "isVoted")
    @Mapping(target = "voteStartAt", source = "votePaper.voteStartAt")
    @Mapping(target = "voteEndAt", source = "votePaper.voteEndAt")
    @Mapping(target = "deliveryAt", source = "votePaper.deliveryAt")
    @Mapping(target = "createdAt", source = "votePaper.createdAt")
    @Mapping(target = "updatedAt", source = "votePaper.updatedAt")
    @Mapping(target = "choices", source = "voteChoices")
    public abstract FullVotePaper fullVotePaper(final RegisteredVotePaper votePaper, final List<RegisteredVoteChoice> voteChoices, Boolean isVoted, VotePaperStatistics votePaperStatistics);

}
