package com.tune_fun.v1.interaction.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.common.entity.BaseEntity;
import com.tune_fun.v1.vote.adapter.output.persistence.VotePaperJpaEntity;
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
@Table(name = "vote_paper_like")
public class VotePaperLikeJpaEntity extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("TSID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_paper_id", nullable = false, updatable = false, referencedColumnName = "id")
    @Comment("투표 게시물 ID")
    private VotePaperJpaEntity votePaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false, updatable = false, referencedColumnName = "id")
    @Comment("투표자 ID")
    private AccountJpaEntity voter;

}
