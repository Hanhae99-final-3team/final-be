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
@Builder
public class ItemResponseDto {
    private Long id;
    private boolean IsMine;
    private String nickname;
    private String title;
    @Lob
    private String content;
    private String petCategory;
    private String itemCategory;
    private List<String> itemImgs;
    private String location;
    private int commentCnt;
    private int zzimCnt;
    private int viewCnt;
    private Long purchasePrice;
    private Long sellingPrice;
    private Long averagePrice;
    private String time;
    private boolean IsComplete;
    private boolean IsZzimed;
    private Long memberId;
}
