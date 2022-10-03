package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.comment.dto.CommentRequestDto;
import com.hanghae.mungnayng.domain.comment.dto.CommentResponseDto;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.CommentRepository;
import com.hanghae.mungnayng.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = {"댓글 API 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @ApiOperation(value = "댓글 등록 메소드")
    @RequestMapping(value = "/items/detail/comments/{itemId}", method = RequestMethod.POST)
    public ResponseEntity<?> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long itemId, @RequestBody CommentRequestDto requestDto) {
        Member member = userDetails.getMember();
        CommentResponseDto ResponseDto = commentService.createComment(member, itemId, requestDto);
        return ResponseEntity.ok()
                .body(ResponseDto);
    }

    @ApiOperation(value = "댓글 조회 메소드")
    @RequestMapping(value = "/items/detail/comments/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<?> getComment(@PathVariable Long itemId) {
        List<CommentResponseDto> responseDtoList = commentService.getComment(itemId);
        int commentCnt = commentRepository.countByItem_Id(itemId);
        return ResponseEntity.ok()
                .body(Map.of("itemId", itemId, "commentCnt", commentCnt, "comments", responseDtoList));
    }

    @ApiOperation(value = "댓글 수정 메소드")
    @RequestMapping(value = "/items/detail/comments/{itemId}/{commentId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long itemId, @PathVariable Long commentId,
                                           @RequestBody CommentRequestDto requestDto) {
        Member member = userDetails.getMember();
        CommentResponseDto responseDto = commentService.updateComment(member, itemId, commentId, requestDto);
        return ResponseEntity.ok()
                .body(responseDto);
    }

    @ApiOperation(value = "댓글 삭제 메소드")
    @RequestMapping(value = "/items/detail/comments/{itemId}/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long itemId, @PathVariable Long commentId) {
        Member member = userDetails.getMember();
        commentService.deleteComment(member, itemId, commentId);
        return ResponseEntity.ok().body(Map.of("success", true, "msg", "삭제 성공"));
    }
}
