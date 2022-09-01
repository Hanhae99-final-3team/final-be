package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.domain.search.ItemSearch;
import com.hanghae.mungnayng.repository.ImageRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final SearchRepository searchRepository;

    // 상품 기본 검색('item - title / content'를 바탕으로)
    @Transactional
    public List<ItemResponseDto> searchItem(String keyword) {
        ItemSearch itemSearch = ItemSearch.builder()
                // :: TODO 검색한 사람 이름 Member(JWT)에서 꺼내넣기, if문으로 로그인하지 않았을 경우에도 빌드되도록
                .nickname("김재영")
                .searchWord(keyword)
                .build();
        searchRepository.save(itemSearch);

        // return 값 -> keyword를 바탕으로 상품 찾아 Dto List 작성
        List<Item> itemList = itemRepository.getAllItemListByTitleOrContent(keyword);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemResponseDtoList.add(
                    buildItemResponseDto(item)
            );
        }


        return itemResponseDtoList;
    }

    // :: TODO isMine 및 isZzimed 기능 구현
    // 공통 작업 - ResponseDto build
    private ItemResponseDto buildItemResponseDto(Item item) {

        // 해당 item의 이미지 호출
        List<Image> imageList = imageRepository.findAllByItemId(item.getId());
        List<String> imgUrlList = new ArrayList<>();
        for (Image image : imageList) {
            System.out.println();
            imgUrlList.add(image.getImgUrl());
        }

//        boolean isMine;
//        isMine = itemRequestDto.getNickname().equals("김재영");
        return ItemResponseDto.builder()
                .id(item.getId())
//                .isMine(isMine)
                .nickname(item.getNickname())
                .title(item.getTitle())
                .content(item.getContent())
                .petCategory(item.getPetCategory())
                .itemCategory(item.getItemCategory())
                .itemImgs(imgUrlList)
                .location(item.getLocation())
                .commentCnt(item.getCommentCnt())
                .zzimCnt(item.getZzimCnt())
                .viewCnt(item.getViewCnt())
                .purchasePrice(item.getPurchasePrice())
                .sellingPrice(item.getSellingPrice())
                .isComplete(item.isComplete())
                .createdAt(item.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .modifiedAt(item.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
