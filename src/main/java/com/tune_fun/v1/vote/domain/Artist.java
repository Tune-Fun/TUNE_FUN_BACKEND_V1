package com.tune_fun.v1.vote.domain;

import java.util.List;

public record Artist(String name, String href, String id, String type, ExternalUrl externalUrl, String uri,
                     List<Image> images, Follower follower, List<String> genres, Integer popularity) {
}
