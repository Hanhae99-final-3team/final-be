package com.hanghae.mungnayng.domain.image;

import com.hanghae.mungnayng.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imgUrl;

    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
}
