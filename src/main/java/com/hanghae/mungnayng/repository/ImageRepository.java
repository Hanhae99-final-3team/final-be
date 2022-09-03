package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByItemId(Long itemId);

    void deleteAllByItemId(Long itemId);

}
