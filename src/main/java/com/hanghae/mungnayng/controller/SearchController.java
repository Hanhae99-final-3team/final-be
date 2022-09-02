package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    // 상품 기본 검색('item - title / content'를 바탕으로)
    @PostMapping("items/search")
    public ResponseEntity<?> searchItem(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("keyword") String keyword){
        return ResponseEntity.ok().body(searchService.searchItem(userDetails, keyword));
    }

    // 최근 검색어
    // :: TODO 닉네임 가져오는 로직 추가
    @GetMapping("items/search")
    public ResponseEntity<?> getSearchWord(){
        return ResponseEntity.ok().body(searchService.getSearchWord());
    }

    // 인기 검색어
    @GetMapping("items/search/popular")
    public ResponseEntity<?> getPopularSearchWord(){
        return ResponseEntity.ok().body(searchService.getPopularSearchWord());
    }
}
