package com.hanghae.mungnayng.domain.item;

import com.hanghae.mungnayng.domain.Timestamped;
import com.hanghae.mungnayng.domain.image.Image;
import com.hanghae.mungnayng.domain.item.dto.ItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

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

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isComplete;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> image;

    public void update(ItemRequestDto itemRequestDto){
        this.title = itemRequestDto.getTitle();
        this.content = itemRequestDto.getContent();
        this.petCategory = itemRequestDto.getPetCategory();
        this.itemCategory = itemRequestDto.getItemCategory();
        this.location = itemRequestDto.getLocation();
        this.purchasePrice = itemRequestDto.getPurchasePrice();
        this.sellingPrice = itemRequestDto.getSellingPrice();
    }

    public void updateZzimCnt(int zzimCnt){
        this.zzimCnt = zzimCnt;
    }

    public void updateIsComplete(boolean isComplete){
        this.isComplete = isComplete;
    }

}
