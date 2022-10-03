package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.search.ItemSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchRepository extends JpaRepository<ItemSearch, Long> {

    /** 최근 검색어 조회 메서드(가장 최근에 검색한 검색어 8개) */
    @Query("select distinct (i.searchWord) from ItemSearch i " +
            "where i.nickname = :nickname")
    Page<String> getAllByNickname(String nickname, Pageable pageable);

    /** 인기 검색어 조회 메서드(가장 빈번하게 검색된 검색어 5개) */
    @Query("select i.searchWord from ItemSearch i " +
            "group by i.searchWord " +
            "order by count (i.searchWord) desc ")
    Page<String> getAllByPopularity(Pageable pageable);

    /** 최근 검색어 전체 조회 메서드(전체 삭제 위함) */
    @Query("select i from ItemSearch i " +
            "where i.nickname = :nickname")
    List<ItemSearch> getAllSearchWordByNickname(String nickname);

    /** 단건 검색기록 조회 메서드(파라미터 값(searchWord)과 Member nickname을 바탕으로 / 최근 검색어 개별 삭제 위함) */
    @Query("select i from ItemSearch i " +
                    "where i.nickname = :nickname and i.searchWord = :searchWord")
    ItemSearch getSearchWordByNicknameAndSearchWord(String nickname, String searchWord);

    /** 검색어 자동완성 메서드 */
    /* TODO :: 추후 가능하다면 ElasticSearch로 해당 메서드 리팩터링 */
    @Query("select distinct (i.searchWord) from ItemSearch i " +
            "where i.searchWord like :keyword% " +
            "group by i.searchWord order by count (i.searchWord) desc ")
    List<String> getSearchwordByKeyword(String keyword);
}
