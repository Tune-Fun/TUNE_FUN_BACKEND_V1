package com.tune_fun.v1.account.adapter.output.persistence.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2AccountRepository extends JpaRepository<OAuth2AccountJpaEntity, Long> {
    Optional<OAuth2AccountJpaEntity> findByEmailAndEnabledTrue(final String email);
}
