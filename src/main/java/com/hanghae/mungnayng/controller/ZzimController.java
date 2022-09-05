package com.hanghae.mungnayng.controller;

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

    /* 상품 찜하기 */
    @PostMapping("items/detail/zzim/{itemId}")
    public ResponseEntity<?> zzimItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails){
        zzimService.itemZzim(itemId, userDetails);
        return ResponseEntity.ok().body(Map.of("isZzimed",true,"msg","찜 되었습니다."));
    }

    /* 찜 취소 */
    @DeleteMapping("items/detail/zzim/{itemId}")
    public ResponseEntity<?> cancelZzim(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails){
        zzimService.cancelZzim(itemId, userDetails);
        return ResponseEntity.ok().body(Map.of("isZzimed",false,"msg","찜 취소되었습니다."));
    }

    /* 내가 찜한 상품 조회 */
    @GetMapping("items/mypage/zzim")
    public ResponseEntity<?> getZzimItem(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok().body(zzimService.getZzimItem(userDetails));
    }

    /* 구매 완료 */
    @PutMapping("items/detail/complete/{itemId}")
    public ResponseEntity<?> purchaseComplete(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails){
        Boolean isComplete = zzimService.purchaseComplete(itemId, userDetails);
        return ResponseEntity.ok().body(Map.of("isComplete",isComplete,"msg","거래 상태가 변경되었습니다."));
    }
}
