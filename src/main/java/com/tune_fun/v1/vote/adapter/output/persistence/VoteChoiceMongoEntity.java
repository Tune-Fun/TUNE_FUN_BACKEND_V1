package com.tune_fun.v1.vote.adapter.output.persistence;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("votePaper")
@Builder
@Getter
public class VoteChoiceMongoEntity {

    @Id
    private String id;

    @DocumentReference(collection = "votePaper", lazy = true)
    private VotePaperMongoEntity votePaper;

}
