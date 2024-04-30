package com.tune_fun.v1.vote.adapter.output.persistence;

import com.tune_fun.v1.account.adapter.output.persistence.AccountJpaEntity;
import com.tune_fun.v1.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "vote_paper")
public class VotePaperJpaEntity extends BaseEntity {

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

    @Column(name = "title", nullable = false)
    @Comment("제목")
    private String title;

    @Column(name = "content", nullable = false)
    @Comment("내용")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false, referencedColumnName = "id")
    @Comment("작성자")
    private AccountJpaEntity author;

    @Enumerated(EnumType.STRING)
    @Column(name = "option", nullable = false)
    @Comment("투표 옵션")
    private VotePaperOption option;

    @Column(name = "vote_start_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("투표 시작일")
    private LocalDateTime voteStartAt;

    @Column(name = "vote_end_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("투표 종료일")
    private LocalDateTime voteEndAt;

    @Column(name = "delivery_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("커버 영상 등록일")
    private LocalDateTime deliveryAt;

}
