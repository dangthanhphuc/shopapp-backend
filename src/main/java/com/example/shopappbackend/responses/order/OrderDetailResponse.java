package com.example.shopappbackend.responses.order;

import com.example.shopappbackend.entities.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("number_of_products")
    private int numberOfProducts;
    @JsonProperty("unit_price")
    private float unitPrice;
    @JsonProperty("total_amount")
    private float totalAmount;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .unitPrice(orderDetail.getUnitPrice())
                .totalAmount(orderDetail.getTotalAmount())
                .build();
    }

}
