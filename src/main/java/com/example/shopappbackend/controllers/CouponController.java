package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.CouponDTO;
import com.example.shopappbackend.entities.Coupon;
import com.example.shopappbackend.repositories.CouponRepository;
import com.example.shopappbackend.services.coupon.ICouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;

    @GetMapping("/calculateCoupon")
    public ResponseEntity<?> calculateCouponValue(
            @RequestParam String couponCode,
            @RequestParam double totalAmount
    ){
        try{
            double finalAmount = couponService.calculateCouponValue(couponCode, totalAmount);
            return ResponseEntity.ok(finalAmount);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
