package com.habin.demo.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("생성일")
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("수정일")
    protected LocalDateTime updatedAt;

}
