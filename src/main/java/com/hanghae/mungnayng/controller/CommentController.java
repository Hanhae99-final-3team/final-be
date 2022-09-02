package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.comment.dto.CommentRequestDto;
import com.hanghae.mungnayng.domain.comment.dto.CommentResponseDto;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.CommentRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;



@RestController
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private  final ItemRepository itemRepository;
//    private final CommentResponseDto commentResponseDto;

    //*댓글 생성
    @RequestMapping(value = "/items/detail/comments/{itemId}", method = RequestMethod.POST)
public ResponseEntity<?> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable Long itemId,
                                       @RequestBody CommentRequestDto requestDto){
        Member member = userDetails.getMember();
        CommentResponseDto ResponseDto = commentService.createComment(member, itemId, requestDto);
        return ResponseEntity.ok()
                .body(ResponseDto);
    }
// *댓글 조회-댓글 카운트랑 작동 확인 후 수정할 것
    @RequestMapping(value = "/items/detail/comments/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<List<CommentResponseDto>> getComment(@PathVariable Long itemId) {
        List<CommentResponseDto> responseDtoList = commentService.getComment(itemId);
        Long commentCnt = commentRepository.count();
        return ResponseEntity.ok()
                .body((List<CommentResponseDto>) Map.of("itemId",itemId,"commentCnt",commentCnt,"comments", responseDtoList));
    }
//*댓글 수정
    @RequestMapping(value = "/items/detail/comments/{itemId}/{commentId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long itemId, @PathVariable Long commentId,
                                           @RequestBody CommentRequestDto requestDto) {
        Member member = userDetails.getMember();
    CommentResponseDto responseDto = commentService.updateComment(member, itemId, commentId, requestDto);
        return ResponseEntity.ok()
                .body(responseDto);
    }

    //*댓글 삭제
    @RequestMapping(value = "/items/detail/comments/{itemId}/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long itemId, @PathVariable Long commentId) {
        Member member = userDetails.getMember();
        commentService.deleteComment(member, itemId, commentId);
        return ResponseEntity.ok().body(Map.of("success",true,"msg","삭제 성공"));

    }


}
