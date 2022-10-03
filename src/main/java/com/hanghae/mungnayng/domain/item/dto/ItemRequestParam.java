package com.hanghae.mungnayng.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class ItemRequestParam {
    @Nullable
    private String petCategory;
    @Nullable
    private String itemCategory;
}
