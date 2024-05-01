package com.tune_fun.v1.vote.adapter.output.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.account.adapter.output.persistence.QAccountJpaEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VoteCustomRepositoryImpl implements VoteCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final QVotePaperJpaEntity VOTE_PAPER = QVotePaperJpaEntity.votePaperJpaEntity;

    private static final QVoteChoiceJpaEntity VOTE_CHOICE = QVoteChoiceJpaEntity.voteChoiceJpaEntity;

    private static final QVoteJpaEntity VOTE = QVoteJpaEntity.voteJpaEntity;

    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;


    @Override
    public List<Long> findVoterIdsByVotePaperUuid(String uuid) {
        return queryFactory.select(ACCOUNT.id)
                .from(VOTE)
                .join(VOTE.voteChoice, VOTE_CHOICE)
                .join(VOTE_CHOICE.votePaper, VOTE_PAPER)
                .join(VOTE.voter, ACCOUNT)
                .where(VOTE_PAPER.uuid.eq(uuid))
                .fetch();
    }

    @Override
    public Optional<VoteJpaEntity> findByVoterUsernameAndVotePaperId(String voter, Long votePaperId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(VOTE)
                        .join(VOTE.voteChoice, VOTE_CHOICE)
                        .join(VOTE_CHOICE.votePaper, VOTE_PAPER)
                        .join(VOTE.voter, ACCOUNT)
                        .where(
                                ACCOUNT.username.eq(voter),
                                VOTE_PAPER.id.eq(votePaperId)
                        )
                        .fetchOne()
        );
    }
}
