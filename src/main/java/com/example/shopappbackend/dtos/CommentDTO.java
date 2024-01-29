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
public class CommentDTO {

    @JsonProperty("product_id")
    @Min(value = 1, message = "The product's id must be >= 1")
    private Long productId;

    @JsonProperty("user_id")
    @Min(value = 1, message = "The user's id must be >= 1")
    private Long userId;

    @JsonProperty("content")
    @NotEmpty(message = "The content of the comment can not be empty")
    private String content;
}
