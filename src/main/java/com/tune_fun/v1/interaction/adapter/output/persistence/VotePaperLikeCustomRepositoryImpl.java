package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.account.adapter.output.persistence.QAccountJpaEntity;
import com.tune_fun.v1.vote.adapter.output.persistence.QVotePaperJpaEntity;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaperLike;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.querydsl.core.types.Projections.fields;

@RequiredArgsConstructor
public class VotePaperLikeCustomRepositoryImpl implements VotePaperLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final QVotePaperLikeJpaEntity VOTE_PAPER_LIKE = QVotePaperLikeJpaEntity.votePaperLikeJpaEntity;
    private static final QVotePaperJpaEntity VOTE_PAPER = QVotePaperJpaEntity.votePaperJpaEntity;
    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;


    @Override
    public Optional<RegisteredVotePaperLike> findByVotePaperIdAndLikerUsername(Long votePaperId, String username) {
        return Optional.ofNullable(
                queryFactory.select(fields(RegisteredVotePaperLike.class,
                                        VOTE_PAPER_LIKE.id.as("id"),
                                        VOTE_PAPER.id.as("votePaperId"),
                                        ACCOUNT.id.as("likerAccountId"),
                                        ACCOUNT.username.as("likerUsername"),
                                        VOTE_PAPER_LIKE.createdAt.as("createdAt")
                                )
                        )
                        .from(VOTE_PAPER_LIKE)
                        .join(VOTE_PAPER_LIKE.votePaper, VOTE_PAPER)
                        .join(VOTE_PAPER_LIKE.liker, ACCOUNT)
                        .where(VOTE_PAPER.id.eq(votePaperId), ACCOUNT.username.eq(username))
                        .fetchOne()
        );
    }

    @Override
    public void deleteByVotePaperIdAndLikerUsername(Long votePaperId, String username) {

        queryFactory.delete(VOTE_PAPER_LIKE)
                .where(
                        VOTE_PAPER_LIKE.votePaper.id.eq(votePaperId)
                                .and(VOTE_PAPER_LIKE.liker.username.eq(username))
                )
                .execute();


    }
}
