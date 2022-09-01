package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.domain.zzim.Zzim;
import com.hanghae.mungnayng.domain.zzim.dto.ZzimRequestDto;
import com.hanghae.mungnayng.repository.ImageRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.repository.ZzimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZzimService {

    private final ItemRepository itemRepository;
    private final ZzimRepository zzimRepository;
    private final ImageRepository imageRepository;

    // 찜 하기
    @Transactional
    public void itemZzim(Long itemId, ZzimRequestDto zzimRequestDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId, zzimRequestDto.getNickname());
        if (optionalZzim.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        }
        Zzim zzim = Zzim.builder()
                .item(item)
                .zzimedBy(zzimRequestDto.getNickname())
                .build();
        zzimRepository.save(zzim);
        int zzimCnt = zzimRepository.countAllByItemId(itemId);
        item.updateZzimCnt(zzimCnt);
    }

    // 찜 취소
    @Transactional
    public void cancelZzim(Long itemId, ZzimRequestDto zzimRequestDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId, zzimRequestDto.getNickname());
        if (!optionalZzim.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        }
        zzimRepository.delete(optionalZzim.get());
        int zzimCnt = zzimRepository.countAllByItemId(itemId);
        item.updateZzimCnt(zzimCnt);
    }

    // 내가 찜한 상품 가져오기
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getZzimItem(ZzimRequestDto zzimRequestDto) {
        List<Zzim> zzimList = zzimRepository.getZzimZzimedByMe(zzimRequestDto.getNickname());
        Collections.reverse(zzimList);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();

        for (Zzim zzim : zzimList) {
            Item item = itemRepository.findByIdOrderByCreatedAtDesc(zzim.getItem().getId());

            // 해당 item의 이미지 호출
            List<Image> imageList = imageRepository.findAllByItemId(item.getId());
            List<String> imgUrlList = new ArrayList<>();
            for (Image image : imageList) {
                System.out.println();
                imgUrlList.add(image.getImgUrl());
            }

            itemResponseDtoList.add(
                    ItemResponseDto.builder()
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
                            .build()
            );
        }
        return itemResponseDtoList;
    }

    // 거래 완료 버튼
    @Transactional
    public Boolean purchaseComplete(Long itemId, ZzimRequestDto zzimRequestDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        // ::TODO 로그인 유효성 검사 추가
        if (item.isComplete() == false) {
            item.updateIsComplete(true);
            return true;
        } else {
            item.updateIsComplete(false);
            return false;
        }
    }
}

