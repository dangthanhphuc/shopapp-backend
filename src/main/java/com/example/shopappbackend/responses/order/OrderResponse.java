package com.example.shopappbackend.responses.order;

import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.entities.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("full_name")
    private String fullname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("email")
    private String email;
    @JsonProperty("status")
    private String status;
    @JsonProperty("note")
    private String note;
    @JsonProperty("create_at")
    private LocalDateTime createdAt;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;

    public static OrderResponse fromOrder(Order order){
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .fullname(order.getFullname())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .status(order.getStatus())
                .note(order.getNote())
                .email(order.getEmail())
                .createdAt(order.getCreatedAt())
                .shippingAddress(order.getShippingAddress())
                .shippingDate(order.getShippingDate())
                .orderDetails(
                        order.getOrderDetails().stream()
                                .map(OrderDetailResponse::fromOrderDetail)
                                .toList()
                )
                .build();
    }
}
