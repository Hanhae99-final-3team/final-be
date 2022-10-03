package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItem_Id(Long itemId);
    int countByItem_Id(Long itemId);
}
