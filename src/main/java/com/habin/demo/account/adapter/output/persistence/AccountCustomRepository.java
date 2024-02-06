package com.habin.demo.account.adapter.output.persistence;

import java.util.Optional;

public interface AccountCustomRepository {

    Optional<AccountJpaEntity> findByUsernameActive(String username);

}
