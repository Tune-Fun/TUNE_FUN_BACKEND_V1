package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.vote.domain.value.RegisteredVotePaperLike;

import java.util.Optional;

public interface VotePaperLikeCustomRepository {

    Optional<RegisteredVotePaperLike> findByVotePaperIdAndLikerUsername(final Long votePaperId, final String username);

    void deleteByVotePaperIdAndLikerUsername(final Long votePaperId, final String username);

}
