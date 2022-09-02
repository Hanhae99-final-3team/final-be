package com.hanghae.mungnayng.domain.comment;

import com.hanghae.mungnayng.domain.Timestamped;
import com.hanghae.mungnayng.domain.comment.dto.CommentRequestDto;
import com.hanghae.mungnayng.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Timestamped {
    @Id
    @JoinColumn(name = "comment_Id")
    private Long Id;
@Column(nullable = false)
    private String content;
@Column(nullable = false)
    private String nickname;

//    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();

    }

}
