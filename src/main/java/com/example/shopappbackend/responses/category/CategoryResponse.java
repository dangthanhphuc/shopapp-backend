package com.example.shopappbackend.responses.category;

import com.example.shopappbackend.entities.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private Map<String, String> errors;

    @JsonProperty("category")
    private Category category;
}
