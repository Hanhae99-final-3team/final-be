package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemMainResponseDto;
import com.hanghae.mungnayng.domain.zzim.Zzim;
import com.hanghae.mungnayng.repository.ImageRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.repository.ZzimRepository;
import com.hanghae.mungnayng.util.TimeUtil;
import com.hanghae.mungnayng.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZzimService {

    private final ItemRepository itemRepository;
    private final ZzimRepository zzimRepository;
    private final ImageRepository imageRepository;
    private final Validator validator;

    /**
     * 찜 하기 메서드
     */
    @Transactional
    public void itemZzim(Long itemId, UserDetails userDetails) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */
        Item item = validator.validateItemExistence(itemId);    /* 상품 존재 여부 유효성 검사 및 반환 */

        String nickname = ((UserDetailsImpl) userDetails).getMember().getNickname();
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId, nickname);

        if (optionalZzim.isPresent()) {
            throw new IllegalArgumentException("이미 찜한 상품입니다.");
        }

        Zzim zzim = Zzim.builder()
                .item(item)
                .zzimedBy(nickname)
                .build();
        zzimRepository.save(zzim);

        int zzimCnt = zzimRepository.countAllByItemId(itemId);

        item.updateZzimCnt(zzimCnt);
    }

    /**
     * 찜 취소 메서드
     */
    @Transactional
    public void cancelZzim(Long itemId, UserDetails userDetails) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */
        Item item = validator.validateItemExistence(itemId);    /* 상품 존재 여부 유효성 검사 및 반환 */

        String nickname = ((UserDetailsImpl) userDetails).getMember().getNickname();
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId, nickname);

        if (optionalZzim.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        }

        zzimRepository.delete(optionalZzim.get());

        int zzimCnt = zzimRepository.countAllByItemId(itemId);

        item.updateZzimCnt(zzimCnt);
    }

    /**
     * 내가 찜한 상품 가져오기 메서드(마이페이지용)
     */
    @Transactional(readOnly = true)
    public List<ItemMainResponseDto> getZzimItem(UserDetails userDetails) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */

        List<Item> itemList = itemRepository.getAllItemListByZzimedId(userDetails.getUsername());
        List<ItemMainResponseDto> itemMainResponseDtoList = new ArrayList<>();

        for (Item item : itemList) {
            // 해당 item의 이미지 호출
            List<Image> imageList = imageRepository.findAllByItemId(item.getId());
            List<String> imgUrlList = new ArrayList<>();
            for (Image image : imageList) {
                System.out.println();
                imgUrlList.add(image.getImgUrl());
            }

            itemMainResponseDtoList.add(
                    ItemMainResponseDto.builder()
                            .id(item.getId())
                            .title(item.getTitle())
                            .petCategory(item.getPetCategory())
                            .itemCategory(item.getItemCategory())
                            .itemImgs(imgUrlList)
                            .location(item.getLocation())
                            .zzimCnt(item.getZzimCnt())
                            .viewCnt(item.getViewCnt())
                            .sellingPrice(item.getSellingPrice())
                            .time(TimeUtil.convertLocaldatetimeToTime(item.getCreatedAt()))
                            .build()
            );
        }
        return itemMainResponseDtoList;
    }

    /**
     * 거래 완료 메서드(상품상세페이지용)
     */
    @Transactional
    public Boolean purchaseComplete(Long itemId, UserDetails userDetails) {
        validator.validateUserDetailsInput(userDetails);   /* 로그인 유효성 검사 */
        Item item = validator.validateItemExistence(itemId);    /* 상품 존재 여부 유효성 검사 및 반환 */
        validator.validateEqualUser(userDetails, item);    /* 작성자와 조회자 일치 여부 유효성 검사 */

        if (!item.isComplete()) {
            item.updateIsComplete(true);
            return true;
        } else {
            item.updateIsComplete(false);
            return false;
        }
    }
}

