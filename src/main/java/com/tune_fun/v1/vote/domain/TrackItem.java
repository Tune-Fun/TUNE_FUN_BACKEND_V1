package com.tune_fun.v1.vote.domain;

import java.util.List;

public record TrackItem(Integer discNumber, Album album, List<String> availableMarkets, Restriction restriction,
                        String type, ExternalIds externalIds, String uri, Integer durationMs, Boolean explicit,
                        Boolean isPlayable, List<Artist> artists, LinkedFrom linkedFrom, String previewUrl,
                        Integer popularity, String name, Integer trackNumber, String href, String id, Boolean isLocal,
                        ExternalUrl externalUrl) {
}
