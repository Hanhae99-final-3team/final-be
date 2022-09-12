package com.hanghae.mungnayng.controller;

import com.hanghae.mungnayng.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = {"검색(상품 검색) API 정보를 제공하는 Controller"})    /* Class를 Swagger의 Resource로 표시, tags -> Swagger UI 페이지에 노출 될 태그 설명 */
@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    /* 상품 기본 검색 - 최신순 정렬('item - title / content'를 바탕으로) */
    @ApiOperation(value = "keyword가 title 혹은 content에 포함되어있는 item을 찾는 검색 메소드 - 최신순 정렬")  /* 특정 경로의 Operation Http Method 설명 */
    @PostMapping("items/search")
    public ResponseEntity<?> searchItem(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok().body(searchService.searchItem(userDetails, keyword));
    }

    /* 상품 기본 검색 - 인기순 정렬 */
    @ApiOperation(value = "keyword가 title 혹은 content에 포함되어있는 item을 찾는 검색 메소드 - 인기순 정렬")
    @PostMapping("items/search/popularity")
    public ResponseEntity<?> searchItemOrderByPopularity(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok().body(searchService.searchItemOrderByPopularity(userDetails, keyword));
    }

    /* 최근 검색어 */
    @ApiOperation(value = "최근 검색한 keyword 8개를 불러오는 메소드")
    @GetMapping("items/search")
    public ResponseEntity<?> getSearchWord(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(searchService.getSearchWord(userDetails));
    }

    /* 최근 검색어 개별 삭제 */
    @ApiOperation(value = "개별 검색 keyword 삭제 메소드")
    @DeleteMapping("items/search")
    public ResponseEntity<?> deleteSerachWord(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("searchWord") String searchWord) {
        searchService.deleteSearchWord(userDetails, searchWord);
        return ResponseEntity.ok().body(Map.of("msg", "검색 키워드 삭제 완료", "success", true));
    }

    /* 최근 검색어 전체 삭제 */
    @ApiOperation(value = "본인이 최근 검색한 keyword 전체 삭제 메소드")
    @DeleteMapping("items/search/all")
    public ResponseEntity<?> deleteAllSearchWord(@AuthenticationPrincipal UserDetails userDetails) {
        searchService.deleteAllSearchWord(userDetails);
        return ResponseEntity.ok().body(Map.of("msg", "최근 검색어 전체 삭제 완료", "success", true));
    }

    /* 인기 검색어 */
    @ApiOperation(value = "가장 빈번하게 검색된 keyword 상위 20개를 불러오는 메소드")
    @GetMapping("items/search/popularity")
    public ResponseEntity<?> getPopularSearchWord() {
        return ResponseEntity.ok().body(searchService.getPopularSearchWord());
    }

    /* 검색어 자동완성 */
    @ApiOperation(value = "검색어 자동완성 메소드")
    @GetMapping("items/search/auto")
    public ResponseEntity<?> geKeywordAutomatically(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(searchService.getKeywordAutomatically(keyword));
    }
}
