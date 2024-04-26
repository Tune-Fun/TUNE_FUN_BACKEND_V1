package com.tune_fun.v1.vote.adapter.output.persistence;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("vote_paper")
@Builder
@Getter
@AllArgsConstructor
public class VotePaperMongoEntity {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("author")
    private String author;

    @Field("option")
    private VotePaperOption option;

    @Field("vote_start_at")
    private LocalDateTime voteStartAt;

    @Field("vote_end_at")
    private LocalDateTime voteEndAt;

    @Builder.Default
    @Field("fcm_sent")
    private boolean fcmSent = false;

    @Field("delivery_at")
    private LocalDateTime deliveryAt;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;


}
