package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.domain.zzim.dto.ZzimRequestDto;
import com.hanghae.mungnayng.service.ZzimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ZzimController {

    private final ZzimService zzimService;

    // 상품 찜하기
    // ::TODO 로그인 연결 후 토큰 값으로 회원정보 받아오도록 수정하고 RequestBody 값이랑 Dto 삭제
    @PostMapping("items/detail/zzim/{itemId}")
    public ResponseEntity<?> zzimItem(@PathVariable Long itemId, @RequestBody ZzimRequestDto zzimRequestDto){
        zzimService.itemZzim(itemId, zzimRequestDto);
        return ResponseEntity.ok().body(Map.of("isZzimed",true,"msg","찜 되었습니다."));
    }

    // 찜 취소
    // ::TODO 로그인 연결 후 토큰 값으로 회원정보 받아오도록 수정하고 RequestBody 값이랑 Dto 삭제
    @DeleteMapping("items/detail/zzim/{itemId}")
    public ResponseEntity<?> cancelZzim(@PathVariable Long itemId, @RequestBody ZzimRequestDto zzimRequestDto){
        zzimService.cancelZzim(itemId, zzimRequestDto);
        return ResponseEntity.ok().body(Map.of("isZzimed",false,"msg","찜 취소되었습니다."));
    }

    // 내가 찜한 상품 조회
    // ::TODO 로그인 연결 후 토큰 값으로 회원정보 받아오도록 수정하고 RequestBody 값이랑 Dto 삭제
    @GetMapping("items/detail/zzim")
    public ResponseEntity<?> getZzimItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ZzimRequestDto zzimRequestDto){
        return ResponseEntity.ok().body(zzimService.getZzimItem(userDetails, zzimRequestDto));
    }

    // 구매 완료
    // ::TODO 로그인 연결 후 토큰 값으로 회원정보 받아오도록 수정하고 RequestBody 값이랑 Dto 삭제
    @PutMapping("items/detail/complete/{itemId}")
    public ResponseEntity<?> purchaseComplete(@PathVariable Long itemId,@RequestBody ZzimRequestDto zzimRequestDto){
        Boolean isComplete = zzimService.purchaseComplete(itemId, zzimRequestDto);
        return ResponseEntity.ok().body(Map.of("isComplete",isComplete,"msg","거래 상태가 변경되었습니다."));
    }
}
