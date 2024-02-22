package com.tune_fun.v1.account.adapter.output.persistence;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tune_fun.v1.common.util.querydsl.PredicateBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;

    @Override
    public Optional<AccountJpaEntity> findActive(final String username, final String email) {
        Predicate predicate = PredicateBuilder.builder()
                .and().eqString(ACCOUNT.username, username)
                .and().eqString(ACCOUNT.email, email)
                .build();

        return Optional.ofNullable(
                queryFactory.selectFrom(ACCOUNT)
                        .where(predicate)
                        .where(ACCOUNT.isEnabled.eq(true))
                        .where(ACCOUNT.isAccountNonLocked.eq(true))
                        .where(ACCOUNT.isAccountNonExpired.eq(true))
                        .where(ACCOUNT.isCredentialsNonExpired.eq(true))
                        .fetchOne()
        );
    }
}
