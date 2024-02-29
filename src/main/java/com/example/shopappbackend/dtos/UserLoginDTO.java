package com.example.shopappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    @JsonProperty("email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
