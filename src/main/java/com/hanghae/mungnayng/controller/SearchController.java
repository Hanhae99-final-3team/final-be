package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    // 상품 기본 검색('item - title / content'를 바탕으로)
    @PostMapping("items/search")
    public ResponseEntity<?> searchItem(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok().body(searchService.searchItem(keyword));
    }

}
