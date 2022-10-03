package com.hanghae.mungnayng.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemRequestDto {
    private String title;
    private String content;
    private String petCategory;
    private String itemCategory;
    private String location;
    private List<MultipartFile> multipartFileList;
    private Long purchasePrice;
    private Long sellingPrice;
}
