package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
