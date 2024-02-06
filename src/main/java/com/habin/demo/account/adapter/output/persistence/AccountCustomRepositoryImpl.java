package com.habin.demo.account.adapter.output.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    private final JPAQueryFactory queryFactory;

    private static final QAccountJpaEntity ACCOUNT = QAccountJpaEntity.accountJpaEntity;

    @Override
    public Optional<AccountJpaEntity> findByUsernameActive(String username) {
        return Optional.ofNullable(
                queryFactory.selectFrom(ACCOUNT)
                        .where(ACCOUNT.username.eq(username))
                        .where(ACCOUNT.isEnabled.eq(true))
                        .where(ACCOUNT.isAccountNonLocked.eq(true))
                        .where(ACCOUNT.isAccountNonExpired.eq(true))
                        .where(ACCOUNT.isCredentialsNonExpired.eq(true))
                        .fetchOne()
        );
    }
}
