package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.repository.ImageRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.repository.MemberRepository;
import com.hanghae.mungnayng.repository.SearchRepository;
import com.hanghae.mungnayng.util.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    ImageRepository imageRepository;
    @Mock
    SearchRepository searchRepository;
    @Mock
    Validator validator;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("상품 검색 정상 동작")
    void searchItem() {
        SearchService searchService = new SearchService(itemRepository, imageRepository, searchRepository, validator, memberRepository);
        UserDetails userDetails = null;
        String toogle = "false";
        String keyword = "sajkdjkasjkldklsajdksajlkdj";
        searchService.searchItem(userDetails, toogle, keyword);
    }

    @Test
    void searchItemOrderByPopularity() {
    }
}