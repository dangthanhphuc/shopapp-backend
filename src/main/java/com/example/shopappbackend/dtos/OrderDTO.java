package com.example.shopappbackend.dtos;

import com.example.shopappbackend.entities.Coupon;
import com.example.shopappbackend.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    @JsonProperty("user_id")
    @Min(value = 1, message = "User's ID must be > 0")
    private Long userId;

    @JsonProperty("coupon_code")
    private String couponCode;

    @JsonProperty("full_name")
    @NotBlank(message = "Please enter a full name")
    private String fullname;

    @JsonProperty("phone_number")
    @NotBlank(message = "Please enter phone number")
    @Size(min = 5, max = 10, message = "Please enter phone number at least 5 characters and maximum length is 10 characters")
    private String phoneNumber;

    @JsonProperty("address")
    @NotBlank(message = "Please enter a address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("email")
    @NotBlank(message = "Please enter email")
    private String email;

    @JsonProperty("total_money")
    @Min(value = 1, message = "The total price must be >= 1")
    private Long totalMoney;

    @JsonProperty("status")
    private String status;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("cart_items")
    @NotNull(message = "Please enter a cart items")
    private List<CartItemDTO> cartItems;
}
