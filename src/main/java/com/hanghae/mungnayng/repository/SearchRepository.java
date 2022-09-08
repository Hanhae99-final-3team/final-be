package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.search.ItemSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<ItemSearch, Long> {

    /* 최근 검색어 조회 - 가장 최근에 검색한 검색어 8개 */
    @Query(nativeQuery = true, value =
            "select DISTINCT (i.search_word) from item_search i " +
                    "where i.nickname = :nickname order by i.id DESC LIMIT 8")
    List<String> getAllByNickname(String nickname);

    /* 인기 검색어 조회 - 가장 빈번하게 검색된 검색어 5개 조회 */
    @Query(nativeQuery = true, value =
            "select search_word from item_search " +
                    "Group By search_word ORDER BY COUNT(search_word) DESC LIMIT 5")
    List<String> getAllByPopularity();

    /* 최근 검색어 전체 조회 */
    @Query(nativeQuery = true, value =
            "select * from item_search i where i.nickname = :nickname")
    List<ItemSearch> getAllSearchWordByNickname(String nickname);

    /* RequestParam으로 받아온 검색어를 Member가 검색한 기록이 있는지 확인, List로 반환 */
    @Query(nativeQuery = true,
            value = "select * from item_search i " +
                    "where i.nickname = :nickname and i.search_word = :searchWord")
    List<ItemSearch> getAllSearchWordByNicknameAndSearchWord(String nickname, String searchWord);
}
