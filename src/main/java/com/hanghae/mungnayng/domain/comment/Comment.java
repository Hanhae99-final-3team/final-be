package com.hanghae.mungnayng.domain.comment;

import com.hanghae.mungnayng.domain.Timestamped;
import com.hanghae.mungnayng.domain.comment.dto.CommentRequestDto;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Timestamped {
    @Id
    @JoinColumn(name = "commentId")
    private Long id;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String nickname;

    @Column
    private LocalDateTime createAt;

//    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;
    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();

    }

}
