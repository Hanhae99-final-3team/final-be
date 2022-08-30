package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.zzim.Zzim;
import com.hanghae.mungnayng.domain.zzim.dto.ZzimRequestDto;
import com.hanghae.mungnayng.repository.ItemRepository;
import com.hanghae.mungnayng.repository.ZzimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZzimService {

    private final ItemRepository itemRepository;
    private final ZzimRepository zzimRepository;

    // 찜 하기
    @Transactional
    public void itemZzim(Long itemId, ZzimRequestDto zzimRequestDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId, zzimRequestDto.getNickname());
        if (optionalZzim.isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        } else {
            Zzim zzim = Zzim.builder()
                    .item(item)
                    .zzimedBy(zzimRequestDto.getNickname())
                    .build();
            zzimRepository.save(zzim);
        }
        int zzimCnt = zzimRepository.countAllByItemId(itemId);
        item.updateZzimCnt(zzimCnt);
    }

    // 찜 취소
    @Transactional
    public void cancelZzim(Long itemId, ZzimRequestDto zzimRequestDto){
        Item item = itemRepository.findById(itemId).orElseThrow(
                ()-> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
        Optional<Zzim> optionalZzim = zzimRepository.findByItemIdAndZzimedBy(itemId, zzimRequestDto.getNickname());
        if(!optionalZzim.isPresent()){
            throw new IllegalArgumentException("유효하지 않은 요청입니다.");
        } else {
            zzimRepository.delete(optionalZzim.get());
        }
    }
}

