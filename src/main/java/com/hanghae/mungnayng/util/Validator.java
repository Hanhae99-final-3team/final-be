package com.hanghae.mungnayng.util;

import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import com.hanghae.mungnayng.repository.ItemRepository;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Data
@Component
public class Validator {
    private final ItemRepository itemRepository;

    /**
     * 상품 등록 메소드 유효성 검사 메소드화
     */
    public void validateCreateItemInput(UserDetails userDetails, ItemRequestDto itemRequestDto) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }
        if (itemRequestDto.getItemCategory() == null || itemRequestDto.getItemCategory().equals("")) {
            throw new IllegalArgumentException("상품 카테고리를 선택해주세요.");
        }
        if (itemRequestDto.getPetCategory() == null || itemRequestDto.getPetCategory().equals("")) {
            throw new IllegalArgumentException("펫 카테고리를 선택해주세요.");
        }
        if (itemRequestDto.getTitle() == null || itemRequestDto.getTitle().equals("")) {
            throw new IllegalArgumentException("상품명을 입력해주세요.");
        }
        if (itemRequestDto.getTitle().length() > 20) {
            throw new IllegalArgumentException("입력 가능한 글자 수를 초과하였습니다.");
        }
        if (itemRequestDto.getContent() == null || itemRequestDto.getContent().equals("")) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        if (itemRequestDto.getPurchasePrice() == null || itemRequestDto.getPurchasePrice() < 0) {
            throw new IllegalArgumentException("정확한 상품 구매 당시 가격을 입력해주세요.");
        }
        if (itemRequestDto.getPurchasePrice().toString().length() > 7) {
            throw new IllegalArgumentException("가격은 백만단위까지만 입력 가능합니다.");
        }
        if (itemRequestDto.getSellingPrice() == null || itemRequestDto.getSellingPrice() < 0) {
            throw new IllegalArgumentException("정확한 판매 희망 가격을 입력해주세요.");
        }
        if (itemRequestDto.getSellingPrice().toString().length() > 7) {
            throw new IllegalArgumentException("가격은 백만단위까지만 입력 가능합니다.");
        }
    }

    /**
     * 공통작업 - 로그인 유효성 검사 메소드화
     */
    public void validateUserDetailsInput(UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }
    }

    /**
     * 상품 존재 여부 유효성 검사 및 반환
     */
    public Item validateItemExistence(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
    }

    /**
     * 작성자와 조회자 일치 여부 유효성 검사
     */
    public void validateEqualUser(UserDetails userDetails, Item item) {
        if (!item.getNickname().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }
    }

    /**
     * 검색 메서드 유효성 검사(null & undefined X)
     */
    public void validateSearchMethod(String keyword) {
        if (keyword == null || keyword.equals("undefined") || keyword.length() < 1) {
            throw new IllegalArgumentException("검색어를 두 글자 이상 입력 후 다시 시도해주세요.");
        }
    }
}