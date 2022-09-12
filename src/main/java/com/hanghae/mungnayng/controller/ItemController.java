package com.hanghae.mungnayng.controller;


import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Api(tags = {"상품(게시글) API 정보를 제공하는 Controller"})    /* Class를 Swagger의 Resource로 표시, tags -> Swagger UI 페이지에 노출 될 태그 설명 */
@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemService itemService;

    /* 상품 등록 */
    @ApiOperation(value = "상품 등록 메소드")  /* 특정 경로의 Operation Http Method 설명 */
    @PostMapping("items")
    public ResponseEntity<?> createItem(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ItemRequestDto itemRequestDto) throws IOException {
        ItemResponseDto itemResponseDto = itemService.createItem(userDetails, itemRequestDto);
        return ResponseEntity.ok().body(itemResponseDto);
    }

    /* 전체 상품 조회(MainPage) */
    @ApiOperation(value = "전체 상품 조회 메소드")
    @GetMapping("items")
    public ResponseEntity<?> getAllItem(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getAllItem(pageable));
    }

    /* 카테고리에 따른 상품 조회(MainPage/단일 카테고리 - petCategory) */
    @ApiOperation(value = "pet_category에 따른 상품 조회 메소드")
    @GetMapping("items/petcategory")
    public ResponseEntity<?> getItemByPetCategory(@RequestParam("petCategory") String petCategory,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getItemByPetCategory(petCategory, pageable));
    }

    /* 카테고리에 따른 상품 조회(MainPage/단일 카테고리 - itemCategory) */
    @ApiOperation(value = "item_category에 따른 상품 조회 메소드")
    @GetMapping("items/itemcategory")
    public ResponseEntity<?> getItemByItemCategory(@RequestParam("itemCategory") String itemCategory,
                                                   @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getItemByItemCategory(itemCategory, pageable));
    }

    /* 카테고리에 따른 상품 조회(MainPage/이중 카테고리) */
    @ApiOperation(value = "카테고리를 2중(pet_category + item_category)으로 반영한 상품 조회 메소드")
    @GetMapping("items/twocategory")
    public ResponseEntity<?> getItemByTwoCategory(@RequestParam("petCategory") String petCategory, @RequestParam("itemCategory") String itemCategory,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getItemByTwoCategory(petCategory, itemCategory, pageable));
    }

    /* 단일 상품 조회(DetailPage) */
    @ApiOperation(value = "단일 상품 조회 메소드")
    @GetMapping("items/detail/{itemId}")
    public ResponseEntity<?> getItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemId,
                                     HttpServletResponse httpServletResponse) {

        Cookie cookie = new Cookie("itemId" + itemId, Long.toString(itemId));    /* itemId로 신규 쿠키 생성(cookie name은 중복불가 */
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);   /* 쿠키 만료 기한은 하루 */
        httpServletResponse.addCookie(cookie);   /* response로 쿠키를 담아 보냄 */

        ItemResponseDto itemResponseDto = itemService.getItem(userDetails, itemId);
        itemService.addViewCnt(itemId);
        return ResponseEntity.ok().header(cookie.getName(), cookie.getValue()).body(itemResponseDto);
    }

    /* 상품 수정 - detail */
    @ApiOperation(value = "상품 수정 메소드")
    @PutMapping("items/detail/{itemId}")
    public ResponseEntity<?> updateItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemId, @ModelAttribute ItemRequestDto itemRequestDto) throws IOException {
        ItemResponseDto itemResponseDto = itemService.updateItem(userDetails, itemId, itemRequestDto);
        return ResponseEntity.ok(itemResponseDto);
    }

    /* 상품 삭제 */
    @ApiOperation(value = "상품 삭제 메소드")
    @DeleteMapping("items/detail/{itemId}")
    public ResponseEntity<?> deleteItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemId) {
        itemService.deleteItem(userDetails, itemId);
        return ResponseEntity.ok().body(Map.of("msg", "게시글이 삭제되었습니다.", "success", true));
    }

    /* 내가 등록한 상품 조회(MyPage) */
    @ApiOperation(value = "내가 등록한 상품 조회 메소드")
    @GetMapping("items/mypage")
    public ResponseEntity<?> getMyItem(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(itemService.getMyItem(userDetails));
    }

    /* 마이페이지 - 차트 */
    @ApiOperation(value = "마이페이지 - 차트 데이터 호출 메소드")
    @GetMapping("items/mypage/charts")
    public ResponseEntity<?> getMyChart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(itemService.getMyChart(userDetails));
    }

    /* 마이페이지 - 내가 조회한 상품 */
    @ApiOperation(value = "마이페이지 - 내가 조회한 상품 호출 메소드")
    @GetMapping("items/mypage/list")
    public ResponseEntity<?> getItemList(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(itemService.getItemList(userDetails, httpServletRequest));
    }
}
