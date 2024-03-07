package com.tune_fun.v1.vote.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("vote")
@Builder
@Getter
@AllArgsConstructor
public class VoteMongoEntity {

    @Id
    private String id;

    @DocumentReference(collection = "vote_paper", lazy = true)
    @Field("vote_paper")
    private VotePaperMongoEntity votePaper;

    @DocumentReference(collection = "vote_choice", lazy = true)
    @Field("vote_choice")
    private VoteChoiceMongoEntity voteChoice;

    @Field("username")
    private String username;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

}
