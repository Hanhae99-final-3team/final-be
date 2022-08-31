package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.repository.ImageRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.util.aws.S3uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final S3uploader s3uploader;
    private final ImageRepository imageRepository;

    // 상품 등록
    public ItemResponseDto createItem(ItemRequestDto itemRequestDto) throws IOException {
        Item item = Item.builder()
                .title(itemRequestDto.getTitle())
                .content(itemRequestDto.getContent())
                // :: TODO 닉네임은 토큰 값에서 가져올 수 있도록 수정
                .nickname(itemRequestDto.getNickname())
                .petCategory(itemRequestDto.getPetCategory())
                .itemCategory(itemRequestDto.getItemCategory())
                .location(itemRequestDto.getLocation())
                .purchasePrice(itemRequestDto.getPurchasePrice())
                .sellingPrice(itemRequestDto.getSellingPrice())
                .build();
        itemRepository.save(item);

        // itemRequestDto에서 받아온 값 중 item과 multipartFile을 따로 저장
        List<MultipartFile> multipartFileList = itemRequestDto.getMultipartFileList();
        if (multipartFileList != null) {
            for (MultipartFile multipartFile : multipartFileList) {
                String url = s3uploader.Uploader(multipartFile);
                Image image = Image.builder()
                        .imgUrl(url)
                        .item(item)
                        .build();
                imageRepository.save(image);
            }
        }

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
    public ItemResponseDto updateItem(Long itemId, ItemRequestDto itemRequestDto) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("조회하시려는 상품이 존재하지 않습니다.")
        );
        item.update(itemRequestDto);

        List<MultipartFile> multipartFileList = itemRequestDto.getMultipartFileList();
        if (multipartFileList != null) {
            imageRepository.deleteAllByItemId(itemId);
            for (MultipartFile multipartFile : multipartFileList) {
                String url = s3uploader.Uploader(multipartFile);
                Image image = Image.builder()
                        .imgUrl(url)
                        .item(item)
                        .build();
                imageRepository.save(image);
            }
        }


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

