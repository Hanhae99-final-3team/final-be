package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.CommentRepository;
import com.hanghae.mungnayng.repository.ImageRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.util.aws.S3uploader;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final  CommentRepository commentRepository;

    /* 상품 등록 */
    public ItemResponseDto createItem(UserDetails userDetails, ItemRequestDto itemRequestDto) throws IOException {
        Item item = Item.builder()
                .title(itemRequestDto.getTitle())
                .content(itemRequestDto.getContent())
                .nickname(((UserDetailsImpl) userDetails).getMember().getNickname())
                .petCategory(itemRequestDto.getPetCategory())
                .itemCategory(itemRequestDto.getItemCategory())
                .location(itemRequestDto.getLocation())
                .purchasePrice(itemRequestDto.getPurchasePrice())
                .sellingPrice(itemRequestDto.getSellingPrice())
                .build();
        itemRepository.save(item);

        /* itemRequestDto에서 받아온 값 중 item과 multipartFile을 따로 저장 */
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
        return buildItemResponseDto(userDetails, item);
    }

    /* 전체 상품 조회 */
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItem(UserDetails userDetails) {
        List<Item> itemList = itemRepository.findAllByOrderByCreatedAtDesc();
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemResponseDtoList.add(
                    buildItemResponseDto(userDetails, item)
            );
        }
        return itemResponseDtoList;
    }

    /* 카테고리에 따른 상품 조회(이중 카테고리) */
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getItemByTwoCategory(UserDetails userDetails, String petCategory, String itemCategory) {
        List<Item> itemList = itemRepository.getAllItemListByTwoCategory(petCategory, itemCategory);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemResponseDtoList.add(
                    buildItemResponseDto(userDetails, item)
            );
        }
        return itemResponseDtoList;
    }

    /* 카테고리에 따른 상품 조회(단일 카테고리 - petCategory) */
    public List<ItemResponseDto> getItemByPetCategory(UserDetails userDetails, String petCategory) {
        List<Item> itemList = itemRepository.getAllItemListByPetCategry(petCategory);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemResponseDtoList.add(
                    buildItemResponseDto(userDetails, item)
            );
        }
        return itemResponseDtoList;
    }

    /* 카테고리에 따른 상품 조회(단일 카테고리 - itemCategory) */
    public List<ItemResponseDto> getItemByItemCategory(UserDetails userDetails, String itemCategory) {
        List<Item> itemList = itemRepository.getAllItemListByItemCategory(itemCategory);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemResponseDtoList.add(
                    buildItemResponseDto(userDetails, item)
            );
        }
        return itemResponseDtoList;
    }

    /* 단일 상품 조회 - detail */
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(UserDetails userDetails, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("조회하시려는 상품이 존재하지 않습니다.")
        );

        return buildItemResponseDto(userDetails, item);
    }

    /* 조회수 증가(단일 상품 조회 시) */
    @Transactional
    public int addViewCnt(Long itemId) {
        return itemRepository.addViewCnt(itemId);
    }


    /* 상품 수정 - detail */
    @Transactional
    public ItemResponseDto updateItem(UserDetails userDetails, Long itemId, ItemRequestDto itemRequestDto) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("조회하시려는 상품이 존재하지 않습니다.")
        );
        if (!item.getNickname().equals(((UserDetailsImpl) userDetails).getMember().getNickname())) {
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }

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


        return buildItemResponseDto(userDetails, item);
    }

    /* 상품 삭제 - detail */
    @Transactional
    public void deleteItem(UserDetails userDetails , Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("조회하시려는 상품이 존재하지 않습니다.")
        );
        if (!item.getNickname().equals(((UserDetailsImpl) userDetails).getMember().getNickname())) {
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }
        itemRepository.delete(item);
    }

    /* TODO isZzimed 기능 구현 -> ItemResponseDto를 ruturn는 모든 곳에*/
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

        return ItemResponseDto.builder()
                .id(item.getId())
                .IsMine(userDetails != null && item.getNickname().equals(((UserDetailsImpl) userDetails).getMember().getNickname()))
                .nickname(item.getNickname())
                .title(item.getTitle())
                .content(item.getContent())
                .petCategory(item.getPetCategory())
                .itemCategory(item.getItemCategory())
                .itemImgs(imgUrlList)
                .location(item.getLocation())
                .zzimCnt(item.getZzimCnt())
                .commentCnt(commentCnt)
                .viewCnt(item.getViewCnt())
                .purchasePrice(item.getPurchasePrice())
                .sellingPrice(item.getSellingPrice())
                .IsComplete(item.isComplete())
                .createdAt(item.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .modifiedAt(item.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}

