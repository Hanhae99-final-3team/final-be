package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.zzim.Zzim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ZzimRepository extends JpaRepository<Zzim, Long> {

    Optional<Zzim> findByItemIdAndZzimedBy(Long itemId, String nickname);

    int countAllByItemId(Long itemId);

    // 내가 찜한 상품 가져오기
    @Query("select z from Zzim z " +
            "where z.zzimedBy = :nickname")
    List<Zzim> getZzimZzimedByMe(String nickname);

}
