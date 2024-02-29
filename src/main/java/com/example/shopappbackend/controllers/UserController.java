package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.UserDTO;
import com.example.shopappbackend.dtos.UserLoginDTO;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.user.UserResponse;
import com.example.shopappbackend.services.user.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
    @GetMapping("")
    public ResponseEntity<?> getAllUsers(){
        try {
            List<UserResponse> userResponses = userService.getUsers().stream()
                    .map(UserResponse::fromUser)
                    .toList();
            return ResponseEntity.ok().body(userResponses);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                Map<String, String> errors = result.getFieldErrors().stream()
                        .filter(error -> error.getDefaultMessage() != null)
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return ResponseEntity.badRequest().body(errors);
            }
            User newUser = userService.createUser(userDTO);
            return ResponseEntity.ok().body(UserResponse.fromUser(newUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserDetail(
            @Valid @PathVariable Long userId,
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                Map<String, String> errors = result.getFieldErrors().stream()
                        .filter(error -> error.getDefaultMessage() != null)
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return ResponseEntity.badRequest().body(errors);
            }
            User updatedUser = userService.updateUserDetails(userId,userDTO);
            return ResponseEntity.ok().body(UserResponse.fromUser(updatedUser));
        } catch (DataNotFoundException | DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @PutMapping("/enable/{userId}/{isUnable}")
    public ResponseEntity<String> unableOrEnable(
            @Valid @PathVariable Long userId,
            @Valid @PathVariable boolean isUnable
    ){
        try {
            userService.unableOrEnabledUser(userId, isUnable);
            String message = isUnable ? "Successfully enabled user !!" : "Successfully unable user !!" ;
            return ResponseEntity.ok().body(message);
        } catch (DataNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Test
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body("Login failed");
            }
            String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
