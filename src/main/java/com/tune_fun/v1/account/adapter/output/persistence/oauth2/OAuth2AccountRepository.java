package com.tune_fun.v1.account.adapter.output.persistence.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2AccountRepository extends JpaRepository<OAuth2AccountJpaEntity, Long> {
}