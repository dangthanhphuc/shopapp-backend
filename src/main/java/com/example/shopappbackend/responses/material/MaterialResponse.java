package com.example.shopappbackend.responses.material;

import com.example.shopappbackend.entities.Category;
import com.example.shopappbackend.entities.Material;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private Map<String, String> errors;

    @JsonProperty("material")
    private Material material;
}
