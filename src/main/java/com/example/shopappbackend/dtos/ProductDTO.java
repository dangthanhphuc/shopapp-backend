package com.example.shopappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @JsonProperty("name")
    @NotEmpty(message = "Product name can not be empty")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("unit_price")
    @Min(value  = 1 , message = "Unit price can not be negative")
    private float unitPrice;

    @JsonProperty("material_id")
    @Min(value = 1 , message = "Material's id can not be negative")
    private Long materialId;

    @JsonProperty("category_id")
    @Min(value = 1 , message = "Category's id can not be negative'")
    private Long categoryId;
}