package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.Notification.Dto.NotificationRequestDto;
import com.hanghae.mungnayng.domain.Notification.Notification;
import com.hanghae.mungnayng.domain.Notification.Type;
import com.hanghae.mungnayng.domain.comment.Comment;
import com.hanghae.mungnayng.domain.comment.dto.CommentRequestDto;
import com.hanghae.mungnayng.domain.comment.dto.CommentResponseDto;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.CommentRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

    private final NotificationRepository notificationRepository;

    /*댓글 생성*/
    @Transactional
    public CommentResponseDto createComment(Member member, Long itemId, CommentRequestDto requestDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));

        Comment comment = Comment.builder()
                .item(item)
                .nickname(member.getNickname())
                .content(requestDto.getContent())
                .build();

        commentRepository.save(comment);
    List<Member> memberList = new ArrayList<>();
        for (Member u : memberList) {
            if(u == member) {
                continue;
            }
            String notification = comment.getId().toString();
            NotificationRequestDto notificationRequestDto = new NotificationRequestDto(Type.댓글, notification);
            notificationRepository.save(new Notification(notificationRequestDto, u));
        }
        return CommentResponseDto.All(comment);
    }

    /*댓글 조회*/
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComment(Long itemId) {
        List<Comment> CommentList = commentRepository.findAllByItem_Id(itemId);
        return CommentList.stream()
/*원본                  .map(value -> CommentResponseDto.All(value) )*/
                .map(CommentResponseDto::All)
                .collect(Collectors.toList());
    }

    /*댓글 수정*/
    @Transactional
    public CommentResponseDto updateComment(Member member, Long itemId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        if (!itemId.equals(comment.getItem().getId()))
            throw new IllegalArgumentException("해당 게시글이 없습니다");
        if (!member.getNickname().equals(comment.getNickname())) {
            throw new IllegalArgumentException("작성자 닉네임이 일치하지 않습니다.");
        }
        comment.update(requestDto);
        return CommentResponseDto.All(comment);
    }

    /*댓글 삭제*/
    @Transactional
    public void deleteComment(Member member, Long itemId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 코멘트가 존재하지 않습니다."));
        if (!itemId.equals(comment.getItem().getId()))
            throw new IllegalArgumentException("해당 게시글이 없습니다");
        if (!member.getNickname().equals(comment.getNickname())) {
            throw new IllegalArgumentException("작성자 닉네임이 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    }

}

