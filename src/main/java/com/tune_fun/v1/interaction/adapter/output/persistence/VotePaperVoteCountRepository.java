package com.tune_fun.v1.interaction.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VotePaperVoteCountRepository extends JpaRepository<VotePaperLikeJpaEntity, Long>, VotePaperLikeCustomRepository {

//    @EntityGraph(attributePaths = {"votePaper", "liker"})
//    void deleteByVotePaperIdAndLikerUsername(final Long votePaperId, final String username);

}
