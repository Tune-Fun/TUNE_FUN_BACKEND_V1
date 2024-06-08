package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "follow",
        indexes = {
                @Index(name = "idx_follower_id", columnList = "follower_id"),
                @Index(name = "idx_followee_id", columnList = "followee_id")
        },
        uniqueConstraints = {@UniqueConstraint(name = "uk_follow", columnNames = {"follower_id", "followee_id"})}
)
public class FollowJpaEntity extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("TSID")
    private Long id;

    @Column(name = "follower_id", nullable = false, updatable = false)
    @Comment("팔로워 ID")
    private Long followerId;

    @Column(name = "followee_id", nullable = false, updatable = false)
    @Comment("팔로이 ID")
    private Long followeeId;

}
