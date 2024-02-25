package com.tune_fun.v1.vote.adapter.output.persistence;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("vote")
@Builder
@Getter
public class VoteMongoEntity {

    @Id
    private String id;



}
