package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOrderByCreatedAtDesc();

    // 상품 단일 조회 시 viewCnt + 1
    @Modifying
    @Query("update Item i set i.viewCnt = i.viewCnt + 1 where i.id = :id")
    int addViewCnt(Long id);

    // petCategory = dog / itemCategory = food
    // 이중 카테고리에 의한 조회
    @Query("select i from Item i " +
            "where i.petCategory = :petCategory and i.itemCategory = :itemCategory " +
            "order by i.createdAt desc ")
    List<Item> getAllItemListByTwoCategory(String petCategory, String itemCategory);

    // 단일 카테고리에 의한 조회 - petCategory
    @Query("select i from Item i " +
            "where i.petCategory = :petCategory " +
            "order by i.createdAt desc ")
    List<Item> getAllItemListByPetCategry(String petCategory);

    // 단일 카테고리에 의한 조회 - itemCategory
    @Query("select i from Item i " +
            "where i.itemCategory = :itemCategory " +
            "order by i.createdAt desc ")
    List<Item> getAllItemListByItemCategory(String itemCategory);
}
