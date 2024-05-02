package com.tune_fun.v1.vote.adapter.output.persistence;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.account.adapter.output.persistence.QAccountJpaEntity;
import com.tune_fun.v1.common.util.querydsl.PredicateBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
public class VotePaperCustomRepositoryImpl implements VotePaperCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final QVotePaperJpaEntity VOTE_PAPER = QVotePaperJpaEntity.votePaperJpaEntity;

    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;

    @Override
    public Optional<VotePaperJpaEntity> findOneAvailable(final Long id, final String username) {

        Predicate predicate = PredicateBuilder.builder()
                .and().eqNumber(VOTE_PAPER.id, id)
                .and().eqString(ACCOUNT.username, username)
                .build();

        return Optional.ofNullable(
                queryFactory.selectFrom(VOTE_PAPER)
                        .join(VOTE_PAPER.author, ACCOUNT)
                        .where(VOTE_PAPER.voteEndAt.gt(now()), predicate)
                        .fetchOne()
        );
    }
}
