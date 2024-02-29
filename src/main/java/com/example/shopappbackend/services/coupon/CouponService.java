package com.example.shopappbackend.services.coupon;

import com.example.shopappbackend.entities.Coupon;
import com.example.shopappbackend.entities.CouponCondition;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.CouponConditionRepository;
import com.example.shopappbackend.repositories.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponService implements ICouponService{
    private final CouponRepository couponRepository;
    private final CouponConditionRepository couponConditionRepository;

    @Override
    public Coupon getCouponByCode(String code) throws DataNotFoundException {
        Coupon coupon = couponRepository.findByCode(code);
        if(coupon == null) {
            throw new DataNotFoundException("Coupon not found");
        }
        return coupon;
    }

    @Override
    public double calculateCouponValue(String couponCode, double totalAmount) throws DataNotFoundException {
        Coupon existingCoupon = getCouponByCode(couponCode);
        return calculateDiscount(existingCoupon, totalAmount);
    }

    private double calculateDiscount(Coupon coupon, double totalAmount) throws DataNotFoundException {
        CouponCondition couponCondition = couponConditionRepository.findByCouponId(coupon.getId())
                .orElseThrow(
                        () -> new DataNotFoundException("Coupon condition not found")
                );

        double discount = 0.0;

        String attribute = couponCondition.getAttribute();
        String operator = couponCondition.getOperator();
        String value = couponCondition.getValue();
        double discountAmount = Double.parseDouble(String.valueOf(couponCondition.getDiscountAmount()));
        if(attribute.equals("minimum_amount")){
            if(operator.equals(">") && totalAmount > Double.parseDouble(value)){
                discount += totalAmount * discountAmount / 100;
            } else if (couponCondition.getOperator().equals("<=") && totalAmount <= Double.parseDouble(value)) {
                discount += totalAmount * discountAmount / 100;
            }
        } else if (attribute.equals("applicable_date")) {
            LocalDate applicableDate = LocalDate.parse(value);
            LocalDate currentDate = LocalDate.now();
            if(operator.equalsIgnoreCase("BETWEEN") && applicableDate.isEqual(currentDate)) {
                discountAmount += totalAmount * discountAmount / 100 ;
            }
        }
        return discount;
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
