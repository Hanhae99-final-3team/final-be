package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.service.ZzimService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = {"찜(좋아요) API 정보를 제공하는 Controller"})    /* Class를 Swagger의 Resource로 표시, tags -> Swagger UI 페이지에 노출 될 태그 설명 */
@RestController
@RequiredArgsConstructor
public class ZzimController {

    private final ZzimService zzimService;

    /* 상품 찜하기 */
    @ApiOperation(value = "상품 찜하기 메소드")  /* 특정 경로의 Operation Http Method 설명 */
    @PostMapping("items/detail/zzim/{itemId}")
    public ResponseEntity<?> zzimItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        zzimService.itemZzim(itemId, userDetails);
        return ResponseEntity.ok().body(Map.of("isZzimed", true, "msg", "찜 되었습니다."));
    }

    /* 찜 취소 */
    @ApiOperation(value = "상품 취소 메소드")
    @DeleteMapping("items/detail/zzim/{itemId}")
    public ResponseEntity<?> cancelZzim(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        zzimService.cancelZzim(itemId, userDetails);
        return ResponseEntity.ok().body(Map.of("isZzimed", false, "msg", "찜 취소되었습니다."));
    }

    /* 내가 찜한 상품 조회 */
    @ApiOperation(value = "내가 찜한 상품 조회 메소드")
    @GetMapping("items/mypage/zzim")
    public ResponseEntity<?> getZzimItem(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(zzimService.getZzimItem(userDetails));
    }

    /* 거래 완료 */
    @ApiOperation(value = "거래 완료 버튼 메소드(구매자가 한 번 누르면 거래 완료('isCompelted' : true) 다시 누르면 거래중(false) 처리")
    @PutMapping("items/detail/complete/{itemId}")
    public ResponseEntity<?> purchaseComplete(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        Boolean isComplete = zzimService.purchaseComplete(itemId, userDetails);
        return ResponseEntity.ok().body(Map.of("isComplete", isComplete, "msg", "거래 상태가 변경되었습니다."));
    }
}
