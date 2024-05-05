package com.tune_fun.v1.vote.application.port.output;

import com.tune_fun.v1.vote.domain.value.RegisteredVotePaper;

public interface UpdateVideoUrlPort {

    RegisteredVotePaper updateVideoUrl(final Long votePaperId, final String videoUrl);

}
