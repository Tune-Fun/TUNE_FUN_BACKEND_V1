package com.tune_fun.v1.vote.adapter.output.persistence;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("votePaper")
@Builder
@Getter
public class VotePaperMongoEntity {

    @Id
    private String id;

    @DocumentReference(collection = "vote", lazy = true)
    private VoteMongoEntity vote;

}
