package com.example.shopappbackend.responses.user;

import com.example.shopappbackend.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("email")
    private String email;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private String address;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("role_id")
    private Long roleId;

    public static UserResponse fromUser(User user){
        return UserResponse.builder()
                .email(user.getEmail())
                .address(user.getAddress())
                .name(user.getFullname())
                .phoneNumber(user.getPhoneNumber())
                .roleId(user.getRole().getId())
                .build();
    }
}
