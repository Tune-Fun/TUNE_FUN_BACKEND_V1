package com.tune_fun.v1.vote.application.service;

import com.tune_fun.v1.common.exception.CommonApplicationException;
import com.tune_fun.v1.common.stereotype.UseCase;
import com.tune_fun.v1.vote.application.port.input.command.VotePaperCommands;
import com.tune_fun.v1.vote.application.port.input.usecase.UpdateVotePaperVideoUrlUseCase;
import com.tune_fun.v1.vote.application.port.output.LoadVotePaperPort;
import com.tune_fun.v1.vote.application.port.output.ProduceVotePaperUpdateVideoUrlPort;
import com.tune_fun.v1.vote.application.port.output.UpdateVideoUrlPort;
import com.tune_fun.v1.vote.domain.event.VotePaperUpdateVideoUrlEvent;
import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import static com.tune_fun.v1.common.response.MessageCode.VOTE_PAPER_NOT_FOUND;
import static com.tune_fun.v1.common.response.MessageCode.VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_VIDEO_URL;

@Service
@UseCase
@RequiredArgsConstructor
public class UpdateVotePaperVideoUrlService implements UpdateVotePaperVideoUrlUseCase {

    private final LoadVotePaperPort loadVotePaperPort;
    private final UpdateVideoUrlPort updateVideoUrlPort;

    private final ProduceVotePaperUpdateVideoUrlPort produceVotePaperUpdateVideoUrlPort;


    @Override
    public void updateVideoUrl(final Long votePaperId, final VotePaperCommands.UpdateVideoUrl command, final User user) {
        RegisteredVotePaper registeredVotePaper = loadVotePaperPort.loadRegisteredVotePaper(votePaperId)
                .orElseThrow(() -> new CommonApplicationException(VOTE_PAPER_NOT_FOUND));

        if (!registeredVotePaper.isAuthor(user.getUsername()))
            throw new CommonApplicationException(VOTE_POLICY_ONLY_AUTHOR_CAN_UPDATE_VIDEO_URL);

        RegisteredVotePaper updatedVotePaper = updateVideoUrlPort.updateVideoUrl(votePaperId, command.videoUrl());

        VotePaperUpdateVideoUrlEvent event = new VotePaperUpdateVideoUrlEvent(updatedVotePaper.uuid(),
                updatedVotePaper.author(), updatedVotePaper.title(), registeredVotePaper.content(), updatedVotePaper.videoUrl());
        produceVotePaperUpdateVideoUrlPort.produceVotePaperUpdateVideoUrlEvent(event);
    }
}
