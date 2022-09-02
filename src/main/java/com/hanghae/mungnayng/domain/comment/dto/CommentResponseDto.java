package com.hanghae.mungnayng.domain.comment.dto;

import com.hanghae.mungnayng.domain.comment.Comment;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isMine;


    public static CommentResponseDto All(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getNickname())
                .createdAt(comment.getCreateAt())
                .isMine(true)
                .build();
    }
}




