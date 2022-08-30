package com.hanghae.mungnayng.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequestDto {
    private String title;
    private String content;
    private String nickname;
    private String petCategory;
    private String itemCategory;
    private String location;
    private String itemImgs;
    private Long purchasePrice;
    private Long sellingPrice;
}
