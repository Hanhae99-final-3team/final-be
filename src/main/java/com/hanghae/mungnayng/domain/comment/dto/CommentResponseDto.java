package com.hanghae.mungnayng.domain.comment.dto;

import com.hanghae.mungnayng.domain.comment.Comment;

import lombok.*;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private String createdAt;
    private String modifiedAt;
    private Boolean isMine;


    public static CommentResponseDto All(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getNickname())
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .modifiedAt(comment.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .isMine(true)
                .build();
    }
}




