package com.tune_fun.v1.account.adapter.output.persistence;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.common.util.querydsl.PredicateBuilder;
import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;
    private final JPAQueryFactory queryFactory;

    private static final BiFunction<Integer, Integer, Boolean> HAS_NEXT = (pageSize, fetchSize) -> fetchSize > pageSize;

    @Override
    public Optional<AccountJpaEntity> findActive(final String username, final String email, final String nickname) {
        Predicate predicate = PredicateBuilder.builder()
                .and().eqString(ACCOUNT.username, username)
                .and().eqString(ACCOUNT.email, email)
                .and().eqString(ACCOUNT.nickname, nickname)
                .build();

        return Optional.ofNullable(
                queryFactory.selectFrom(ACCOUNT)
                        .where(predicate)
                        .where(ACCOUNT.enabled.eq(true))
                        .where(ACCOUNT.accountNonLocked.eq(true))
                        .where(ACCOUNT.accountNonExpired.eq(true))
                        .where(ACCOUNT.credentialsNonExpired.eq(true))
                        .fetchOne()
        );
    }

    @Override
    public Slice<ScrollableArtist> scrollArtist(final Pageable pageable, final Long lastId, final String nickname) {
        Predicate predicate = PredicateBuilder.builder()
                .and().containsString(ACCOUNT.nickname, nickname)
                .and().ltNumber(ACCOUNT.id, lastId)
                .build();

        List<ScrollableArtist> fetch = queryFactory.select(
                        Projections.fields(ScrollableArtist.class,
                                ACCOUNT.id,
                                ACCOUNT.username,
                                ACCOUNT.nickname,
                                ACCOUNT.profileImageUrl
                        )
                )
                .from(ACCOUNT)
                .where(predicate)
                .orderBy(ACCOUNT.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (HAS_NEXT.apply(pageable.getPageSize(), fetch.size())) {
            fetch.removeLast();
            hasNext = true;
        }

        return new SliceImpl<>(fetch, pageable, hasNext);
    }

}
