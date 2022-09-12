package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAll(Pageable pageable);

    /* 상품 리스트 호출 시 LastData(마지막 게시글)인지 확인 */
    @Query("select min(i.id) from Item i")   /* 전체 상품 조회 */
    int lastData();
    @Query("select min(i.id) from Item i where i.petCategory = :petCategory")    /* 펫 카테고리 */
    int lastDataPetCategory(String petCategory);
    @Query("select min(i.id) from Item i where i.itemCategory = :itemCategory")    /* 아이템 카테고리 */
    int lastDataItemCategory(String itemCategory);
    @Query("select min(i.id) from Item i where i.petCategory = :petCategory and i.itemCategory = :itemCategory")
    int lastDataTwoCategory(String petCategory, String itemCategory);   /* 이중 카테고리 */

    /* 상품 단일 조회 시 viewCnt + 1 */
    @Modifying
    @Query("update Item i set i.viewCnt = i.viewCnt + 1 where i.id = :id")
    int addViewCnt(Long id);

    /* 이중 카테고리에 의한 조회 */
    @Query("select i from Item i " +
            "where i.petCategory = :petCategory and i.itemCategory = :itemCategory ")
    Page<Item> getAllItemListByTwoCategory(String petCategory, String itemCategory, Pageable pageable);

    /* 단일 카테고리에 의한 조회 - petCategory */
    @Query("select i from Item i " +
            "where i.petCategory = :petCategory ")
    Page<Item> getAllItemListByPetCategry(String petCategory, Pageable pageable);

    /* 단일 카테고리에 의한 조회 - itemCategory */
    @Query("select i from Item i " +
            "where i.itemCategory = :itemCategory ")
    Page<Item> getAllItemListByItemCategory(String itemCategory, Pageable pageable);

//    /* 상품 기본 검색 - 최신순 정렬('item - title / content'를 바탕으로) */
    @Query("select i from Item i " +
            "where i.title like %:keyword% or i.content like %:keyword% or " +
            "i.itemCategory like %:keyword% or i.petCategory like %:keyword% " +
            "order by i.createdAt desc ")
//    @Query(nativeQuery = true, value =
//            "select * from item i " +
//                    "where INSTR(i.title, :keyword) > 0 or INSTR(i.content, :keyword) > 0 " +
//                    "order by i.id desc ")
//    @Query(nativeQuery = true, value =
//            "select * from item i " +
//                    "where i.title IN :keyword or i.content IN :keyword " +
//                    "order by i.created_at desc ")
    List<Item> getAllItemListByTitleOrContent(String keyword);

    /* 상품 기본 검색 - 인기순 정렬 */
    @Query("select i from Item i " +
            "where i.title like %:keyword% or i.content like %:keyword% or " +
            "i.itemCategory like %:keyword% or i.petCategory like %:keyword% " +
            "order by i.viewCnt desc ")
    List<Item> getAllItemListByOrderByPopularity(String keyword);

    /* 내가 찜한 상품 조회 */
    @Query(nativeQuery = true, value =
    "select * from item i inner join zzim z on i.id = z.item_id " +
            "where z.zzimed_by = :nickname order by z.id desc")
    List<Item> getAllItemListByZzimedId(String nickname);

    /* 내가 등록한 상품 조회 */
    @Query("select i from Item i " +
            "where i.nickname = :nickname order by i.createdAt desc ")
    List<Item> getAllItemByNickname(String nickname);

    /* 마이페이지 - 차트용 데이터 호출(자기 등록 상품 가격의 총합) */
    @Query("select sum(i.sellingPrice) from Item i " +
            "where i.nickname = :nickname group by i.nickname")
    int getFirstItemsPriceSum(String nickname);

    /* 내가 등록한 상품 중 판매 완료 상품 조회 */
    @Query("select i from Item i " +
            "where i.nickname = :nickname and i.isComplete = true")
    List<Item> getAllSoldItemByNickname(String nickname);

    /* 마이페이지 - 차트용 데이터 호출(판매 완료된 자기 등록 상품 가격의 총합) */
    @Query("select sum(i.sellingPrice) from Item i " +
            "where i.isComplete = true and i.nickname = :nickname group by i.nickname")
    int getSecondItemsPriceSum(String nickname);

    /* 마이페이지 - 차트용 데이터 호출(내가 찜한 상품 가격의 총합) */
    @Query(nativeQuery = true, value =
            "select sum(selling_price) from item i inner join zzim z " +
                    "on z.item_id = i.id where z.zzimed_by = :nickname")
    int getThirdItemsPriceSum(String nickname);

    /* itemCategory 등록상품의 평균 가격 호출 */
    @Query("select avg(i.sellingPrice) from Item i " +
            "where i.itemCategory = :itemCategory ")
    Long getAveragePrice(String itemCategory);
}
