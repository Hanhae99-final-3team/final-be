package com.hanghae.mungnayng.controller;


import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemService itemService;

    /* 상품 등록 */
    @PostMapping("items")
    public ResponseEntity<?> createItem(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ItemRequestDto itemRequestDto) throws IOException {
        ItemResponseDto itemResponseDto = itemService.createItem(userDetails, itemRequestDto);
        return ResponseEntity.ok().body(itemResponseDto);
    }

    /* 전체 상품 조회 */
    @GetMapping("items")
    public ResponseEntity<?> getAllItem(@AuthenticationPrincipal UserDetails userDetails,
                                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getAllItem(userDetails, pageable));
    }

    /* 카테고리에 따른 상품 조회(단일 카테고리 - petCategory) */
    @GetMapping("items/petcategory")
    public ResponseEntity<?> getItemByPetCategory(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("petCategory") String petCategory,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getItemByPetCategory(userDetails, petCategory, pageable));
    }

    /* 카테고리에 따른 상품 조회(단일 카테고리 - itemCategory) */
    @GetMapping("items/itemcategory")
    public ResponseEntity<?> getItemByItemCategory(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("itemCategory") String itemCategory,
                                                   @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getItemByItemCategory(userDetails, itemCategory, pageable));
    }

    /* 카테고리에 따른 상품 조회(이중 카테고리) */
    @GetMapping("items/twocategory")
    public ResponseEntity<?> getItemByTwoCategory(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestParam("petCategory") String petCategory, @RequestParam("itemCategory") String itemCategory,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok().body(itemService.getItemByTwoCategory(userDetails, petCategory, itemCategory, pageable));
    }

    /* 단일 상품 조회 - detail */
    @GetMapping("items/detail/{itemId}")
    public ResponseEntity<?> getItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemId) {
        ItemResponseDto itemResponseDto = itemService.getItem(userDetails, itemId);
        itemService.addViewCnt(itemId);
        return ResponseEntity.ok().body(itemResponseDto);
    }

    /* 상품 수정 - detail */
    @PutMapping("items/detail/{itemId}")
    public ResponseEntity<?> updateItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemId, @ModelAttribute ItemRequestDto itemRequestDto) throws IOException {
        ItemResponseDto itemResponseDto = itemService.updateItem(userDetails, itemId, itemRequestDto);
        return ResponseEntity.ok(itemResponseDto);
    }

    /* 상품 삭제 */
    @DeleteMapping("items/detail/{itemId}")
    public ResponseEntity<?> deleteItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemId) {
        itemService.deleteItem(userDetails, itemId);
        return ResponseEntity.ok().body(Map.of("msg", "게시글이 삭제되었습니다.", "success", true));
    }
}
