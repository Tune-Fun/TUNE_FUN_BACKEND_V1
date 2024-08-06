package com.tune_fun.v1.account.adapter.output.persistence;

import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, Long>, AccountCustomRepository {

    Window<AccountJpaEntity> findFirst10ByEnabledTrue(KeysetScrollPosition position);

}
