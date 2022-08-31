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
}
