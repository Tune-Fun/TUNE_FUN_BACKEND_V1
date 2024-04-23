package com.tune_fun.v1.vote.domain;

import java.util.List;

public record ArtistsItem(String name, String href, String id, String type, ExternalUrls externalUrls, String uri,
                          List<ImagesItem> images, Followers followers, List<String> genres, Integer popularity) {
}
