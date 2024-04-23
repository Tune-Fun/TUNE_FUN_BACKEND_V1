package com.tune_fun.v1.vote.domain;

import java.util.List;

public record Album(List<ImagesItem> images, List<String> availableMarkets, Restrictions restrictions,
					String releaseDatePrecision, String type, String uri, Integer totalTracks, String releaseDate,
					List<ArtistsItem> artists, String name, String albumType, String href, String id,
					ExternalUrls externalUrls) {
}
