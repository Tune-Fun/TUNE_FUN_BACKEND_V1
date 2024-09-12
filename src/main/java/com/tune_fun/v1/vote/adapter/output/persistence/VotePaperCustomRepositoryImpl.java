package com.tune_fun.v1.vote.adapter.output.persistence;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.account.adapter.output.persistence.QAccountJpaEntity;
import com.tune_fun.v1.common.util.querydsl.PredicateBuilder;
import com.tune_fun.v1.interaction.adapter.output.persistence.QVotePaperLikeJpaEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
public class VotePaperCustomRepositoryImpl implements VotePaperCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final QVotePaperJpaEntity VOTE_PAPER = QVotePaperJpaEntity.votePaperJpaEntity;

    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;

    private static final QVotePaperLikeJpaEntity VOTE_PAPER_LIKE = QVotePaperLikeJpaEntity.votePaperLikeJpaEntity;

    private static final QVoteJpaEntity VOTE = QVoteJpaEntity.voteJpaEntity;

    private static final QVoteChoiceJpaEntity VOTE_CHOICE = QVoteChoiceJpaEntity.voteChoiceJpaEntity;

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

    @Override
    public List<UserInteractedVotePaper> findLikedByUsernameBeforeLastTimeAndLastId(final String username, final Long lastId, final LocalDateTime lastTime, final int limit) {
        return queryFactory.select(
                        Projections.constructor(UserInteractedVotePaper.class,
                                VOTE_PAPER.id,
                                VOTE_PAPER.uuid,
                                VOTE_PAPER.title,
                                VOTE_PAPER.content,
                                VOTE_PAPER.pageLink,
                                VOTE_PAPER.author.username,
                                VOTE_PAPER.author.nickname,
                                VOTE_PAPER.option,
                                VOTE_PAPER.voteStartAt,
                                VOTE_PAPER.voteEndAt,
                                VOTE_PAPER.deliveryAt,
                                VOTE_PAPER.enabled,
                                VOTE_PAPER.videoUrl,
                                VOTE_PAPER_LIKE.createdAt)
                )
                .from(VOTE_PAPER)
                .join(VOTE_PAPER.likes, VOTE_PAPER_LIKE)
                .join(VOTE_PAPER_LIKE.liker, ACCOUNT)
                .where(
                        ACCOUNT.username.eq(username),
                        beforeVotePaperLiked(lastTime),
                        ltVotePaperId(lastId),
                        VOTE_PAPER.enabled.eq(true)
                )
                .orderBy(VOTE_PAPER_LIKE.createdAt.desc(), VOTE_PAPER.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Long> findLikedIdsByUsernameAndIds(String username, Set<Long> ids) {
        return queryFactory.select(VOTE_PAPER.id)
                .from(VOTE_PAPER)
                .join(VOTE_PAPER.likes, VOTE_PAPER_LIKE)
                .join(VOTE_PAPER_LIKE.liker, ACCOUNT)
                .where(
                        VOTE_PAPER.id.in(ids),
                        ACCOUNT.username.eq(username)
                )
                .fetch();
    }

    @Override
    public List<UserInteractedVotePaper> findParticipatedByUsernameBeforeLastId(final String username, final Long lastId, final LocalDateTime lastTime, final int limit) {
        return queryFactory.select(
                        Projections.constructor(UserInteractedVotePaper.class,
                                VOTE_PAPER.id,
                                VOTE_PAPER.uuid,
                                VOTE_PAPER.title,
                                VOTE_PAPER.content,
                                VOTE_PAPER.pageLink,
                                VOTE_PAPER.author.username,
                                VOTE_PAPER.author.nickname,
                                VOTE_PAPER.option,
                                VOTE_PAPER.voteStartAt,
                                VOTE_PAPER.voteEndAt,
                                VOTE_PAPER.deliveryAt,
                                VOTE_PAPER.enabled,
                                VOTE_PAPER.videoUrl,
                                VOTE.createdAt)
                )
                .from(VOTE_PAPER)
                .leftJoin(VOTE_PAPER.choices, VOTE_CHOICE)
                .leftJoin(VOTE_CHOICE.votes, VOTE)
                .leftJoin(VOTE.voter, ACCOUNT)
                .where(
                        beforeVotePaperVoted(lastTime),
                        ltVotePaperId(lastId),
                        ACCOUNT.username.eq(username)
                )
                .orderBy(VOTE_CHOICE.createdAt.desc(), VOTE_PAPER.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Long> findParticipatedVotePaperIdsByUsername(final String username, final Set<Long> ids) {
        return queryFactory.select(VOTE_PAPER.id)
                .from(VOTE_PAPER)
                .leftJoin(VOTE_PAPER.choices, VOTE_CHOICE)
                .leftJoin(VOTE_CHOICE.votes, VOTE)
                .leftJoin(VOTE.voter, ACCOUNT)
                .where(
                        VOTE_PAPER.id.in(ids),
                        ACCOUNT.username.eq(username),
                        VOTE_PAPER.enabled.eq(true)
                )
                .fetch();
    }

    private BooleanExpression ltVotePaperId(Long lastId) {
        return lastId != null ? VOTE_PAPER.id.lt(lastId) : null;
    }

    private BooleanExpression beforeVotePaperLiked(LocalDateTime lastTime) {
        return lastTime != null ? VOTE_PAPER_LIKE.createdAt.before(lastTime) : null;
    }

    private BooleanExpression beforeVotePaperVoted(LocalDateTime lastTime) {
        return lastTime != null ? VOTE_CHOICE.createdAt.before(lastTime) : null;
    }
}
