package com.tune_fun.v1.vote.application.port.input.usecase;

import com.tune_fun.v1.vote.domain.value.FullVotePaper;
import org.springframework.security.core.userdetails.User;

@FunctionalInterface
public interface GetVotePaperUseCase {

    FullVotePaper getVotePaper(final Long votePaperId, final User user);

}
