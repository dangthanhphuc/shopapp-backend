package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.CouponDTO;
import com.example.shopappbackend.entities.Coupon;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.CouponRepository;
import com.example.shopappbackend.responses.ResponseObject;
import com.example.shopappbackend.responses.comment.CommentResponse;
import com.example.shopappbackend.services.coupon.ICouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;

    @GetMapping("/calculateCoupon")
    public ResponseEntity<ResponseObject> calculateCouponValue(
            @RequestParam String couponCode,
            @RequestParam double totalAmount
    ) throws DataNotFoundException {

        double finalAmount = couponService.calculateCouponValue(couponCode, totalAmount);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Calculate coupon successfully !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(finalAmount)
                        .build()
        );


    }
}
