package com.example.shopappbackend.repositories;

import com.example.shopappbackend.entities.CouponCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponConditionRepository extends JpaRepository<CouponCondition, Long> {}

