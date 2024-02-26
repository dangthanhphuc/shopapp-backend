package com.example.shopappbackend.responses.product;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPage;
}
