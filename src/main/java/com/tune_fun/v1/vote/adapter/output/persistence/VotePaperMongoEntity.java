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

@Document("vote_paper")
@Builder
@Getter
@AllArgsConstructor
public class VotePaperMongoEntity {

    @Id
    @Field("vote_paper_id")
    private String votePaperId;

    @Field("uuid")
    private String uuid;

    @DocumentReference(collection = "vote", lazy = true)
    private VoteMongoEntity vote;

    @Field("option")
    private VotePaperOption option;

    @Field("delivery_at")
    private LocalDateTime deliveryAt;

    @Field("vote_end_at")
    private LocalDateTime voteEndAt;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

}
