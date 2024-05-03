package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "vote_choice")
public class VoteChoiceJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("Sequence")
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "uuid", nullable = false)
    @Comment("고유번호")
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "vote_paper_id", nullable = false, updatable = false, referencedColumnName = "id")
    @Comment("투표 게시물 ID")
    private VotePaperJpaEntity votePaper;

    @JdbcTypeCode(SqlTypes.JSON)
    @Embedded
    @Comment("선택지 제안사항")
    private Offer offer;

    @Singular
    @OneToMany(mappedBy = "voteChoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VoteJpaEntity> votes;

}
