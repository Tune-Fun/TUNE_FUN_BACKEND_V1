package com.tune_fun.v1.vote.domain;

import java.util.List;

public record Album(List<Image> images, List<String> availableMarkets, Restriction restriction,
					String releaseDatePrecision, String type, String uri, Integer totalTracks, String releaseDate,
					List<Artist> artists, String name, String albumType, String href, String id,
					ExternalUrl externalUrl) {
}
