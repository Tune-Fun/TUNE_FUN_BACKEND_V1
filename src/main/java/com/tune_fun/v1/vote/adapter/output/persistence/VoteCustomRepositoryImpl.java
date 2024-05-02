package com.tune_fun.v1.vote.adapter.output.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.account.adapter.output.persistence.QAccountJpaEntity;
import com.tune_fun.v1.vote.domain.value.RegisteredVote;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Projections.fields;

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
    public Optional<RegisteredVote> findByVoterUsernameAndVotePaperId(String voter, Long votePaperId) {
        RegisteredVote registeredVote = queryFactory.select(fields(RegisteredVote.class,
                                VOTE.id,
                                VOTE.uuid,
                                ACCOUNT.username,
                                VOTE_PAPER.id.as("votePaperId"),
                                VOTE_CHOICE.offer.music,
                                VOTE_CHOICE.offer.artistName
                        )
                )
                .from(VOTE)
                .join(VOTE.voteChoice, VOTE_CHOICE)
                .join(VOTE_CHOICE.votePaper, VOTE_PAPER)
                .join(VOTE.voter, ACCOUNT)
                .where(
                        ACCOUNT.username.eq(voter),
                        VOTE_PAPER.id.eq(votePaperId)
                )
                .fetchOne();

        return Optional.ofNullable(registeredVote);
    }
}
