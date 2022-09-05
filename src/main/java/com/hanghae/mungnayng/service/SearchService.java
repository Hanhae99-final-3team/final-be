package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.domain.search.ItemSearch;
import com.hanghae.mungnayng.domain.search.dto.ItemSearchResponsedto;
import com.hanghae.mungnayng.domain.zzim.Zzim;
import com.hanghae.mungnayng.repository.*;
import com.hanghae.mungnayng.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final SearchRepository searchRepository;
    private final CommentRepository commentRepository;
    private final ZzimRepository zzimRepository;

    /* 상품 기본 검색('item - title / content'를 바탕으로) */
    @Transactional
    public List<ItemResponseDto> searchItem(UserDetails userDetails, String keyword) {
        String nickname = "null";
        if (userDetails != null){
            nickname = ((UserDetailsImpl)userDetails).getMember().getNickname();
        }

        ItemSearch itemSearch = ItemSearch.builder()
                .nickname(nickname)
                .searchWord(keyword)
                .build();
        searchRepository.save(itemSearch);

        // return 값 -> keyword를 바탕으로 상품 찾아 Dto List 작성
        List<Item> itemList = itemRepository.getAllItemListByTitleOrContent(keyword);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemResponseDtoList.add(
                    buildItemResponseDto(userDetails, item)
            );
        }


        return itemResponseDtoList;
    }

    /* 공통 작업 - ResponseDto build */
    private ItemResponseDto buildItemResponseDto(UserDetails userDetails, Item item) {
        int commentCnt = commentRepository.countByItem_Id(item.getId());

        // 해당 item의 이미지 호출
        List<Image> imageList = imageRepository.findAllByItemId(item.getId());
        List<String> imgUrlList = new ArrayList<>();
        for (Image image : imageList) {
            System.out.println();
            imgUrlList.add(image.getImgUrl());
        }

        /* IsZzimed - 사용자가 찜한 상품인지 아닌지 확인 */
        boolean isZzimed = false;
        if(userDetails != null) {
            Optional<Zzim> zzim = zzimRepository.findByItemIdAndZzimedBy(item.getId(), userDetails.getUsername());
            if(zzim.isPresent()) isZzimed = true;
        }

        return ItemResponseDto.builder()
                .id(item.getId())
                .IsMine(userDetails != null && item.getNickname().equals(userDetails.getUsername()))
                .nickname(item.getNickname())
                .title(item.getTitle())
                .content(item.getContent())
                .petCategory(item.getPetCategory())
                .itemCategory(item.getItemCategory())
                .itemImgs(imgUrlList)
                .commentCnt(commentCnt)
                .location(item.getLocation())
                .zzimCnt(item.getZzimCnt())
                .viewCnt(item.getViewCnt())
                .purchasePrice(item.getPurchasePrice())
                .sellingPrice(item.getSellingPrice())
                .IsComplete(item.isComplete())
                .IsZzimed(isZzimed)
                .createdAt(item.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .modifiedAt(item.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .time(TimeUtil.convertLocaldatetimeToTime(item.getCreatedAt()))
                .build();
    }

    /* 최근 검색어 조회 */
    public List<ItemSearchResponsedto> getSearchWord(UserDetails userDetails) {
        List<ItemSearchResponsedto> itemSearchResponsedtoList = new ArrayList<>();

        if (userDetails==null) {    /* 비로그인 시 빈 배열 출력 */
            return itemSearchResponsedtoList;
        }

        String nickname = ((UserDetailsImpl)userDetails).getMember().getNickname();
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
