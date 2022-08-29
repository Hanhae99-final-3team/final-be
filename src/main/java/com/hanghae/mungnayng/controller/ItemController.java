package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemRepository itemRepository;

    private final ItemService itemService;

    // 상품 등록
    // : TODO 로그인 시만 가능하도록 기능 추가
    @PostMapping("items")
    public ResponseEntity<?> createItem(@RequestBody ItemRequestDto itemRequestDto) {
        ItemResponseDto itemResponseDto = itemService.createItem(itemRequestDto);
        return ResponseEntity.ok().body(itemResponseDto);
    }

    // 단일 상품 조회 - detail
    @GetMapping("items/detail/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        ItemResponseDto itemResponseDto = itemService.getItem(itemId);
        return ResponseEntity.ok().body(itemResponseDto);
    }

    // 상품 수정 - detail
    @PutMapping("items/detail/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId, @RequestBody ItemRequestDto itemRequestDto) {
        ItemResponseDto itemResponseDto = itemService.updateItem(itemId, itemRequestDto);
        return ResponseEntity.ok(itemResponseDto);
    }

    // 상품 삭제
    @DeleteMapping("items/detail/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId){
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().body(Map.of("msg","게시글이 삭제되었습니다.","success",true));
    }
}
