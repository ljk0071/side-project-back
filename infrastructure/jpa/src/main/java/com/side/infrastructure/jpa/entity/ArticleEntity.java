package com.side.infrastructure.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Embeddable
public class ArticleEntity {

    @Column(name = "title", length = 100, nullable = false)
    @Comment("게시글제목")
    private String title;

    @Column(name = "contents", length = 255, nullable = false)
    @Comment("게시글내용")
    private String contents;
}
