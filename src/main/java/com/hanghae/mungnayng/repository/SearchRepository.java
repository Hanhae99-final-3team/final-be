package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.search.ItemSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<ItemSearch, Long> {
}
