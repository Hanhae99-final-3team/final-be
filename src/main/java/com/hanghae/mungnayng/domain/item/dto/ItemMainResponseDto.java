package com.hanghae.mungnayng.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder    /* Main Page에 노출될 item의 정보만 불러오는 ResponseDto */
public class ItemMainResponseDto {
    private Long id;
    private String title;
    private String petCategory;
    private String itemCategory;
    private List<String> itemImgs;
    private String location;
    private int zzimCnt;
    private int viewCnt;
    private Long sellingPrice;
    private String time;
    private boolean IsComplete;
}
