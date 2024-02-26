package com.example.shopappbackend.repositories;

import com.example.shopappbackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProductId(@Param("productId") Long productId);

    List<Comment> findByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);

}