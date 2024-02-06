package com.tune_fun.v1.account.adapter.output.persistence;

import java.util.Optional;

public interface AccountCustomRepository {

    Optional<AccountJpaEntity> findByUsernameActive(String username);

}
