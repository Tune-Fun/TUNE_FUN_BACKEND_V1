package com.tune_fun.v1.vote.adapter.output.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

import static com.querydsl.core.group.GroupBy.groupBy;

@RequiredArgsConstructor
public class VotePaperStatisticsCustomRepositoryImpl implements VotePaperStatisticsCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public static final QVotePaperStatisticsJpaEntity VOTE_PAPER_STATISTICS = QVotePaperStatisticsJpaEntity.votePaperStatisticsJpaEntity;

    @Transactional
    @Override
    public void updateLikeCount(Long votePaperId, Long likeCount) {
        queryFactory.update(VOTE_PAPER_STATISTICS)
                .set(VOTE_PAPER_STATISTICS.likeCount, likeCount)
                .where(VOTE_PAPER_STATISTICS.votePaperId.eq(votePaperId))
                .execute();

        entityManager.clear();
        entityManager.flush();
    }

    @Transactional
    @Override
    public void updateVoteCount(Long votePaperId, Long voteCount) {
        queryFactory.update(VOTE_PAPER_STATISTICS)
                .set(VOTE_PAPER_STATISTICS.likeCount, voteCount)
                .where(VOTE_PAPER_STATISTICS.votePaperId.eq(votePaperId))
                .execute();

        entityManager.clear();
        entityManager.flush();
    }

    @Override
    public Map<Long, Long> findLikeCountMap(Set<Long> votePaperIds) {
        return queryFactory.from(VOTE_PAPER_STATISTICS)
                .where(VOTE_PAPER_STATISTICS.votePaperId.in(votePaperIds))
                .transform(groupBy(VOTE_PAPER_STATISTICS.votePaperId).as(VOTE_PAPER_STATISTICS.likeCount));
    }
}
