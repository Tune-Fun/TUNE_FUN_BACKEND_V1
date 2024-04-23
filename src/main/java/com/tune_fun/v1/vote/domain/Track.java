package com.tune_fun.v1.vote.domain;

import java.util.List;

public record Track(String next, Integer total, Integer offset, String previous, Integer limit, String href,
                    List<TrackItem> items) {
}
