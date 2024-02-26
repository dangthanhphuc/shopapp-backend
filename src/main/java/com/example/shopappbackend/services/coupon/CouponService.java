package com.example.shopappbackend.services.coupon;

import com.example.shopappbackend.entities.Coupon;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponService implements ICouponService{
    private final CouponRepository couponRepository;

    @Override
    public Coupon getCouponByCode(String code) throws DataNotFoundException {
        Coupon coupon = couponRepository.findByCode(code);
        if(coupon == null) {
            throw new DataNotFoundException("Coupon not found");
        }
        return coupon;
    }

    @Override
    public List<Coupon> getCoupons() {
        return couponRepository.findAll();
    }

    private Coupon getCouponById(Long id) throws DataNotFoundException {
        return couponRepository.findById(id)
                .filter(coupon -> !coupon.isDeleted())
                .orElseThrow(
                        () -> new DataNotFoundException("Coupon not found")
                );
    }


}
