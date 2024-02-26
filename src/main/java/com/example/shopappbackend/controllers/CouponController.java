package com.example.shopappbackend.controllers;

import com.example.shopappbackend.entities.Coupon;
import com.example.shopappbackend.repositories.CouponRepository;
import com.example.shopappbackend.services.coupon.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;
    @GetMapping("")
    private ResponseEntity<List<Coupon>> getCoupons () {
        return ResponseEntity.ok().body(couponService.getCoupons());
    }

}
