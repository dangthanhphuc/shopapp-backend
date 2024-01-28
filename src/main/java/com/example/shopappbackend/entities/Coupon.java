package com.example.shopappbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Coupon is not empty")
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;


}
