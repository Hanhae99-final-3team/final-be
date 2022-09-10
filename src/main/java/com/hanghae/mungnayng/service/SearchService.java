package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemMainResponseDto;;
import com.hanghae.mungnayng.domain.search.ItemSearch;
import com.hanghae.mungnayng.domain.search.dto.ItemSearchResponsedto;
import com.hanghae.mungnayng.repository.*;
import com.hanghae.mungnayng.util.TimeUtil;
import com.hanghae.mungnayng.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final SearchRepository searchRepository;
    private final Validator validator;

    /* 상품 기본 검색 - 최신순('item - title / content'를 바탕으로) */
    @Transactional
    public List<ItemMainResponseDto> searchItem(UserDetails userDetails, String keyword) {
        String nickname = "nonMember";  /* 비회원으로 검색할 경우 nickname은 nonMember로 저장 */
        if (userDetails != null){
            nickname = (userDetails.getUsername());
        }

        ItemSearch itemSearch = ItemSearch.builder()
                .nickname(nickname)
                .searchWord(keyword)
                .build();
        searchRepository.save(itemSearch);

        /* return 값 -> keyword를 바탕으로 상품 찾아 Dto List 작성 */
        List<Item> itemList = itemRepository.getAllItemListByTitleOrContent(keyword);
        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemMainResponseDtoList.add(
                    buildItemMainResponseDto(item)
            );
        }

        return itemMainResponseDtoList;
    }

    /* 상품 기본 검색 - 인기순('item - title / content'를 바탕으로) */
    @Transactional
    public List<ItemMainResponseDto> searchItemOrderByPopularity(UserDetails userDetails, String keyword) {
        String nickname = "null";
        if (userDetails != null){
            nickname = (userDetails.getUsername());
        }

        ItemSearch itemSearch = ItemSearch.builder()
                .nickname(nickname)
                .searchWord(keyword)
                .build();
        searchRepository.save(itemSearch);

        /* return 값 -> keyword를 바탕으로 상품 찾아 Dto List 작성 */
        List<Item> itemList = itemRepository.getAllItemListByOrderByPopularity(keyword);
        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemMainResponseDtoList.add(
                    buildItemMainResponseDto(item)
            );
        }

        return itemMainResponseDtoList;
    }

    /* 공통 작업 - ResponseDto build */
    private ItemMainResponseDto buildItemMainResponseDto(Item item) {

        // 해당 item의 이미지 호출
        List<Image> imageList = imageRepository.findAllByItemId(item.getId());
        List<String> imgUrlList = new ArrayList<>();
        for (Image image : imageList) {
            System.out.println();
            imgUrlList.add(image.getImgUrl());
        }

        return ItemMainResponseDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .petCategory(item.getPetCategory())
                .itemCategory(item.getItemCategory())
                .itemImgs(imgUrlList)
                .location(item.getLocation())
                .zzimCnt(item.getZzimCnt())
                .viewCnt(item.getViewCnt())
                .sellingPrice(item.getSellingPrice())
                .IsComplete(item.isComplete())
                .time(TimeUtil.convertLocaldatetimeToTime(item.getCreatedAt()))
                .build();
    }

    /* 최근 검색어 조회 */
    public List<ItemSearchResponsedto> getSearchWord(UserDetails userDetails) {
        List<ItemSearchResponsedto> itemSearchResponsedtoList = new ArrayList<>();

        if (userDetails == null) {    /* 비로그인 시 빈 배열 출력 */
            return itemSearchResponsedtoList;
        }

        String nickname = (userDetails.getUsername());
        List<String> searchWordList = searchRepository.getAllByNickname(nickname);
        for (String string : searchWordList) {
            itemSearchResponsedtoList.add(
                    ItemSearchResponsedto.builder()
                            .searchWord(string)
                            .build()
            );
        }
        return itemSearchResponsedtoList;
    }

    /* 최근 검색어 개별 삭제*/
    public void deleteSearchWord(UserDetails userDetails, String searchWord){
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */

        List<ItemSearch> itemSearchList = searchRepository.getAllSearchWordByNicknameAndSearchWord(userDetails.getUsername(),searchWord);
        searchRepository.deleteAll(itemSearchList);
    }

    /* 최근 검색어 전체 삭제 */
    public void deleteAllSearchWord(UserDetails userDetails){
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */

        List<ItemSearch> itemSearchList = searchRepository.getAllSearchWordByNickname(userDetails.getUsername());
        searchRepository.deleteAll(itemSearchList);
    }

    /* 인기 검색어 조회 */
    public List<ItemSearchResponsedto> getPopularSearchWord() {
        List<String> searchWordList = searchRepository.getAllByPopularity();
        List<ItemSearchResponsedto> itemSearchResponsedtoList = new ArrayList<>();
        for(String string : searchWordList){
            itemSearchResponsedtoList.add(
              ItemSearchResponsedto.builder()
                      .searchWord(string)
                      .build()
            );
        }
        return itemSearchResponsedtoList;
    }
}
