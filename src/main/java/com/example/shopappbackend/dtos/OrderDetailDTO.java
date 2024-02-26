package com.example.shopappbackend.dtos;

import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.entities.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "The number of products must be >= 1")
    private int numberOfProducts;

    @JsonProperty("product_id")
    @Min(value = 1, message = "The product's id must be >= 1")
    private Long productId;

    @JsonProperty("order_id")
    @Min(value = 1, message = "The order's id must be >= 1")
    private Long orderId;
}
