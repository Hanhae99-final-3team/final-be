package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * 전체 상품 리스트 조회
     */
    Page<Item> findAll(Pageable pageable);

    /**
     * 상품 리스트 호출 시 LastData(마지막 게시글)인지 확인
     */
    @Query("select min(i.id) from Item i")   /* 전체 상품 조회 */
    int lastData();

    @Query("select min(i.id) from Item i where i.petCategory = :petCategory")    /* 펫 카테고리 */
    int lastDataPetCategory(String petCategory);

    @Query("select min(i.id) from Item i where i.itemCategory = :itemCategory")    /* 아이템 카테고리 */
    int lastDataItemCategory(String itemCategory);

    @Query("select min(i.id) from Item i where i.petCategory = :petCategory and i.itemCategory = :itemCategory")
    int lastDataTwoCategory(String petCategory, String itemCategory);   /* 이중 카테고리 */

    /**
     * 상품 단일 조회 시 viewCnt + 1
     */
    @Modifying
    @Query("update Item i set i.viewCnt = i.viewCnt + 1 where i.id = :id")
    int addViewCnt(Long id);

    /**
     * 이중 카테고리에 의한 조회
     */
    @Query("select i from Item i " +
            "where i.petCategory = :petCategory and i.itemCategory = :itemCategory ")
    Page<Item> getAllItemListByTwoCategory(String petCategory, String itemCategory, Pageable pageable);

    /**
     * 단일 카테고리에 의한 조회 - petCategory
     */
    /* TODO :: queryDSL 사용하여 동적 쿼리로 작성(itemCategory로 조회하는 메소드와 합칠 것) */
    @Query("select i from Item i " +
            "where i.petCategory = :petCategory ")
    Page<Item> getAllItemListByPetCategry(String petCategory, Pageable pageable);

    /**
     * 단일 카테고리에 의한 조회 - itemCategory
     */
    @Query("select i from Item i " +
            "where i.itemCategory = :itemCategory ")
    Page<Item> getAllItemListByItemCategory(String itemCategory, Pageable pageable);

    /**
     * 상품 기본 검색 - 최신순 정렬
     */
    @Query("select i from Item i " +
            "where i.title like %:keyword% or i.content like %:keyword% or " +
            "i.itemCategory like %:keyword% or i.petCategory like %:keyword% " +
            "order by i.createdAt desc ")
    List<Item> getAllItemListByTitleOrContent(String keyword);

    /**
     * 상품 기본 검색 - 인기순 정렬
     */
    @Query("select i from Item i " +
            "where i.title like %:keyword% or i.content like %:keyword% or " +
            "i.itemCategory like %:keyword% or i.petCategory like %:keyword% " +
            "order by i.viewCnt desc ")
    List<Item> getAllItemListByOrderByPopularity(String keyword);

    /**
     * 내가 찜한 상품 조회
     */
    @Query("select i from Item i " +
            "inner join Zzim z on i.id = z.item.id " +
            "where z.zzimedBy = :nickname order by z.id desc")
    List<Item> getAllItemListByZzimedId(String nickname);

    /**
     * 내가 등록한 상품 조회
     */
    @Query("select i from Item i " +
            "where i.nickname = :nickname order by i.createdAt desc ")
    List<Item> getAllItemByNickname(String nickname);

    /**
     * 마이페이지 - 차트용 데이터 호출(자기 등록 상품 가격의 총합)
     */
    @Query("select sum(i.sellingPrice) from Item i " +
            "where i.nickname = :nickname group by i.nickname")
    int getFirstItemsPriceSum(String nickname);

    /**
     * 내가 등록한 상품 중 판매 완료 상품 조회
     */
    @Query("select i from Item i " +
            "where i.nickname = :nickname and i.isComplete = true")
    List<Item> getAllSoldItemByNickname(String nickname);

    /**
     * 마이페이지 - 차트용 데이터 호출(판매 완료된 자기 등록 상품 가격의 총합)
     */
    @Query("select sum(i.sellingPrice) from Item i " +
            "where i.isComplete = true and i.nickname = :nickname group by i.nickname")
    int getSecondItemsPriceSum(String nickname);

    /**
     * 마이페이지 - 차트용 데이터 호출(내가 찜한 상품 가격의 총합)
     */
    @Query("select sum(i.sellingPrice) from Item i " +
            "inner join Zzim z on z.item.id = i.id " +
            "where z.zzimedBy = :nickname")
    int getThirdItemsPriceSum(String nickname);

    /**
     * 특정 itemCategory 등록상품 100개 리스트화 메소드(평균 가격 도출 위함)
     */
    List<Item> getTop100ByItemCategoryOrderByIdDesc(@Param("itemCategory") String itemCategory);
}
