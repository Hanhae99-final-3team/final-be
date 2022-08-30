package com.hanghae.mungnayng.domain.item;

import com.hanghae.mungnayng.domain.Timestamped;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Item extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname;

    @Column
    private String title;

    @Lob
    @Column
    private String content;

    // 1차분류 - 강아지 or 고양이
    @Column
    private String petCategory;

    // 2차분류 - 사료, 간식, 의류, 미용, 장난감, 기타용품
    @Column
    private String itemCategory;

    @Column
    private String itemImgs;

    @Column
    private String location;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int commentCnt;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int zzimCnt;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCnt;

    @Column
    private Long purchasePrice;

    @Column
    private Long sellingPrice;

    public void update(ItemRequestDto itemRequestDto){
        this.title = itemRequestDto.getTitle();
        this.content = itemRequestDto.getContent();
        this.petCategory = itemRequestDto.getPetCategory();
        this.itemCategory = itemRequestDto.getItemCategory();
        // :: TODO 이미지 제대로 수정되는지 확인
        this.itemImgs = itemRequestDto.getItemImgs();
        this.location = itemRequestDto.getLocation();
    }

    public void updateZzimCnt(int zzimCnt){
        this.zzimCnt = zzimCnt;
    }

}
