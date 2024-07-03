package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.vote.application.port.input.usecase.DeleteVotePaperUseCase;
import com.tune_fun.v1.vote.application.port.output.DeleteVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@UseCase
@RequiredArgsConstructor
public class DeleteVotePaperService implements DeleteVotePaperUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final DeleteVotePaperPort deleteVotePaperPort;

    @Transactional
    @Override
    public void delete(final Long votePaperId, final User user) {
        RegisteredVotePaper registeredVotePaper = loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                .orElseThrow(CommonApplicationException.VOTE_PAPER_NOT_FOUND);

        if (!registeredVotePaper.isAuthor(user.getUsername()))
            throw CommonApplicationException.VOTE_POLICY_ONLY_AUTHOR_CAN_DELETE;

        deleteVotePaperPort.disableVotePaper(votePaperId);
    }
}
