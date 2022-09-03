package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.UserDetailsImpl;
import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemResponseDto;
import com.hanghae.mungnayng.domain.zzim.Zzim;
import com.hanghae.mungnayng.repository.CommentRepository;
import com.hanghae.mungnayng.repository.ImageRepository;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.repository.ZzimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final CommentRepository commentRepository;

    /* 찜 하기 */
    @Transactional
    public void itemZzim(Long itemId, UserDetails userDetails) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        String nickname = ((UserDetailsImpl)userDetails).getMember().getNickname();
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId,nickname);
        if (optionalZzim.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        }
        Zzim zzim = Zzim.builder()
                .item(item)
                .zzimedBy(nickname)
                .build();
        zzimRepository.save(zzim);
        int zzimCnt = zzimRepository.countAllByItemId(itemId);
        item.updateZzimCnt(zzimCnt);
    }

    /* 찜 취소 */
    @Transactional
    public void cancelZzim(Long itemId, UserDetails userDetails) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        String nickname = ((UserDetailsImpl)userDetails).getMember().getNickname();
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId, nickname);
        if (!optionalZzim.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        }
        zzimRepository.delete(optionalZzim.get());
        int zzimCnt = zzimRepository.countAllByItemId(itemId);
        item.updateZzimCnt(zzimCnt);
    }

    /* 내가 찜한 상품 가져오기 */
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getZzimItem(UserDetails userDetails) {
        if (userDetails == null){
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        }
        String nickname = ((UserDetailsImpl)userDetails).getMember().getNickname();

        List<Item> itemList = itemRepository.getAllItemListByZzimedId(nickname);
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        
        for (Item item : itemList) {
            // 해당 item의 이미지 호출
            List<Image> imageList = imageRepository.findAllByItemId(item.getId());
            List<String> imgUrlList = new ArrayList<>();
            for (Image image : imageList) {
                System.out.println();
                imgUrlList.add(image.getImgUrl());
            }

            int commentCnt = commentRepository.countByItem_Id(item.getId());
            
            itemResponseDtoList.add(
                    ItemResponseDto.builder()
                            .id(item.getId())
                            .IsMine(item.getNickname().equals(nickname))
                            .nickname(item.getNickname())
                            .title(item.getTitle())
                            .content(item.getContent())
                            .petCategory(item.getPetCategory())
                            .itemCategory(item.getItemCategory())
                            .itemImgs(imgUrlList)
                            .location(item.getLocation())
                            .commentCnt(commentCnt)
                            .zzimCnt(item.getZzimCnt())
                            .viewCnt(item.getViewCnt())
                            .purchasePrice(item.getPurchasePrice())
                            .sellingPrice(item.getSellingPrice())
                            .IsComplete(item.isComplete())
                            .createdAt(item.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                            .modifiedAt(item.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                            .build()
            );
        }
        return itemResponseDtoList;
    }

    /* 거래 완료 버튼 */
    @Transactional
    public Boolean purchaseComplete(Long itemId, UserDetails userDetails) {
        if(userDetails == null){
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        }
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        if (!item.getNickname().equals(((UserDetailsImpl) userDetails).getMember().getNickname())) {
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }

        if (item.isComplete() == false) {
            item.updateIsComplete(true);
            return true;
        } else {
            item.updateIsComplete(false);
            return false;
        }
    }
}

