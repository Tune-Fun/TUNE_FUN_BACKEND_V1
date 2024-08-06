package com.tune_fun.v1.account.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, Long>, AccountCustomRepository {

}
