package com.tune_fun.v1.account.adapter.output.persistence;

import java.util.Optional;

public interface AccountCustomRepository {

    Optional<AccountJpaEntity> findActive(final String username, final String email, final String nickname);

}
