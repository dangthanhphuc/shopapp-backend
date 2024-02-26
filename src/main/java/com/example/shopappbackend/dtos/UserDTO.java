package com.example.shopappbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @JsonProperty("email")
    @NotEmpty(message = "The email is required")
    private String email;

    @JsonProperty("password")
    @NotEmpty(message = "The password is required")
    private String password;

    @JsonProperty("full_name")
    @NotEmpty(message = "User name is required")
    private String fullname;

    @JsonProperty("address")
    @NotEmpty(message = "Address must be provided")
    private String address;

    @JsonProperty("phone_number")
    @Size(min = 10, max = 11, message = "Please enter phone number between 10 and 11 digits")
    private String phoneNumber;

}
