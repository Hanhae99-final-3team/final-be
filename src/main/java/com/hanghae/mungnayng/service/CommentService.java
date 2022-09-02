package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.comment.Comment;
import com.hanghae.mungnayng.domain.comment.dto.CommentRequestDto;
import com.hanghae.mungnayng.domain.comment.dto.CommentResponseDto;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.CommentRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

//댓글 생성
    @Transactional
public CommentResponseDto createComment(Member member,Long itemId ,CommentRequestDto requestDto) {
    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException( "게시글이 없습니다."));
        Comment comment = Comment.builder()
                .item(item)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        return CommentResponseDto.All(comment);
    }
//*댓글 조회(댓글 카운트 값, 게시물 아이디 값 아직 안 넣음)//
    @Transactional
    public List<CommentResponseDto> getComment(Long itemId) {
        List<Comment> allByCommentList = commentRepository.findAllById(itemId);
            return allByCommentList.stream()
                    .map(CommentResponseDto :: All)
                    .collect(Collectors.toList());
    }
//*댓글 수정//
    @Transactional
    public CommentResponseDto updateComment(Member member,Long itemId, Long commentId,  CommentRequestDto requestDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        if (!member.getNickname().equals(comment.getNickname())) {
            throw new IllegalArgumentException("작성자 닉네임이 일치하지 않습니다.");
        }
        comment.update(requestDto);
        return CommentResponseDto.All(comment);
    }

    //*댓글 삭제 미완//
    @Transactional
        public void deleteComment(Member member, Long itemId, Long commentId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("해당 코멘트가 존재하지 않습니다."));
        if(!member.getNickname().equals(comment.getNickname())) {
            throw new IllegalArgumentException("작성자 닉네임이 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    }

    }


//    @Transactional(readOnly = true)
//public Comment isPresentComment(Long itemId, Long commentId) {
//        Optional<Comment> comment = Optional.ofNullable(commentRepository.findByItemIdAndId(itemId, commentId));
//        return comment.orElseThrow(
//                ()-> new IllegalArgumentException("잘못된 요청입니다.")


