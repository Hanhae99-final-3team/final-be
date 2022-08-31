package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByItemId(Long itemId);
    List<Image> deleteAllByItemId(Long itemId);
}
