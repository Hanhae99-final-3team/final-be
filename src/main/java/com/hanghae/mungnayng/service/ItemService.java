package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    // 상품 등록
    public ItemResponseDto createItem(ItemRequestDto itemRequestDto) {
        Item item = Item.builder()
                .title(itemRequestDto.getTitle())
                .content(itemRequestDto.getContent())
                // :: TODO 닉네임은 토큰 값에서 가져올 수 있도록 수정
                .nickname(itemRequestDto.getNickname())
                .petCategory(itemRequestDto.getPetCategory())
                .itemCategory(itemRequestDto.getItemCategory())
                .location(itemRequestDto.getLocation())
                // :: TODO 이미지 S3를 통한 다중 이미지 업로드 기능 추가
                .itemImgs(itemRequestDto.getItemImgs())
                .purchasePrice(itemRequestDto.getPurchasePrice())
                .sellingPrice(itemRequestDto.getSellingPrice())
                .build();

        itemRepository.save(item);
        return buildItemResponseDto(item);
    }

    // 단일 상품 조회 - detail
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("조회하시려는 상품이 존재하지 않습니다.")
        );
        return buildItemResponseDto(item);
    }

    // 조회수 증가(단일 상품 조회 시)
    @Transactional
    public int addViewCnt(Long itemId) {
        return itemRepository.addViewCnt(itemId);
    }

    // 상품 수정 - detail
    @Transactional
    public ItemResponseDto updateItem(Long itemId, ItemRequestDto itemRequestDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("조회하시려는 상품이 존재하지 않습니다.")
        );
        item.update(itemRequestDto);
        return buildItemResponseDto(item);
    }

    // 상품 삭제 - detail
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("조회하시려는 상품이 존재하지 않습니다.")
        );
        itemRepository.delete(item);
    }

    // :: TODO isMine 및 isZzimed 기능 구현
    // 공통 작업 - ResponseDto build
    private ItemResponseDto buildItemResponseDto(Item item) {
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
                .itemImgs(item.getItemImgs())
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

