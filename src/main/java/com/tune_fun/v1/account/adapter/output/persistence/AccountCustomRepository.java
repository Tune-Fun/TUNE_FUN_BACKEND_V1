package com.tune_fun.v1.account.adapter.output.persistence;

import com.tune_fun.v1.interaction.domain.ScrollableArtist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface AccountCustomRepository {

    Optional<AccountJpaEntity> findActive(final String username, final String email, final String nickname);

    Slice<ScrollableArtist> scrollArtist(final Pageable pageable, final Long lastId, final String nickname);

}
