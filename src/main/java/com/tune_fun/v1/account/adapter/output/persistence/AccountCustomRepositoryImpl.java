package com.tune_fun.v1.account.adapter.output.persistence;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.common.util.querydsl.PredicateBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;
    private final JPAQueryFactory queryFactory;

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
}
