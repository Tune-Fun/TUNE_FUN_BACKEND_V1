package com.tune_fun.v1.music.adapter.output.persistence;

import com.tune_fun.v1.common.hexagon.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class MusicPersistenceAdapter {

    private final MusicRepository musicRepository;

}
