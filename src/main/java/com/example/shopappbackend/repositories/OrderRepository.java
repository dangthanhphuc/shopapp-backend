package com.example.shopappbackend.repositories;

import com.example.shopappbackend.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query(value = "SELECT * FROM orders o WHERE o.is_deleted = false AND " +
            "(:keyword IS NULL OR :keyword = '' "+
            "OR o.full_name LIKE %:keyword% " +
            "OR o.address LIKE %:keyword% " +
            "OR o.phone_number LIKE %:keyword% " +
            "OR o.note LIKE %:keyword% " +
            "OR o.email LIKE %:keyword%) ", nativeQuery = true)
    Page<Order> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
