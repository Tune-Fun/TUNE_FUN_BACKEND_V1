package com.tune_fun.v1.account.adapter.output.persistence.social;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("social_account")
@Builder
@Getter
@AllArgsConstructor
public class SocialAccountMongoEntity {

    @Id
    private String id;

    @Field("uuid")
    private String uuid;

    @Field("nickname")
    private String nickname;

    @Field("profile_image_url")
    private String profileImageUrl;

    @Field("oauth_provider")
    private String oauthProvider;

    @Field("oauth_id")
    private String oauthId;

    @Field(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field(name = "withdrawal_at")
    private LocalDateTime withdrawalAt;

    @Field("deleted_at")
    protected LocalDateTime deletedAt;

}
