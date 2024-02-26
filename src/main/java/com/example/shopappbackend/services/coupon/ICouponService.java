package com.example.shopappbackend.services.coupon;

import com.example.shopappbackend.entities.Coupon;
import com.example.shopappbackend.exceptions.DataNotFoundException;

import java.util.List;

public interface ICouponService {
    Coupon getCouponByCode(String code) throws DataNotFoundException;

    List<Coupon> getCoupons();
}
