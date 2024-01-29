package com.example.shopappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {

    @JsonProperty("img_url")
    @Size(min = 5, max = 200, message = "Image's name")
    private  String imgUrl;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's id must be greater than 0")
    private Long productId;
}
