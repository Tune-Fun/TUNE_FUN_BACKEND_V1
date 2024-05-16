package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "vote_statistics")
public class VoteStatisticsJpaEntity extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("TSID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_paper_id", nullable = false, updatable = false, referencedColumnName = "id")
    @Comment("투표 게시물 ID")
    private VotePaperJpaEntity votePaper;

    @Builder.Default
    @Column(name = "like_count", nullable = false)
    @Comment("좋아요 수")
    private Long likeCount = 0L;

    @Builder.Default
    @Column(name = "vote_count", nullable = false)
    @Comment("투표 수")
    private Long voteCount = 0L;


}
