package com.example.shopappbackend.controllers;

import com.example.shopappbackend.components.JwtTokenUtils;
import com.example.shopappbackend.dtos.UserDTO;
import com.example.shopappbackend.dtos.UserLoginDTO;
import com.example.shopappbackend.entities.Token;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.exceptions.ExpiredTokenException;
import com.example.shopappbackend.exceptions.InvalidPasswordException;
import com.example.shopappbackend.responses.ResponseObject;
import com.example.shopappbackend.responses.user.LoginResponse;
import com.example.shopappbackend.responses.user.UserResponse;
import com.example.shopappbackend.services.token.ITokenService;
import com.example.shopappbackend.services.user.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
    private final ITokenService tokenService;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getAllUsers() throws DataNotFoundException {

        List<UserResponse> userResponses = userService.getUsers().stream()
                .map(UserResponse::fromUser)
                .toList();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Get users successfully !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(userResponses)
                        .build()
        );

    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) throws DataNotFoundException, InvalidPasswordException {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(error -> error.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Input is invalid !")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .data(errors)
                            .build()
            );
        }
        User newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Created user successfully !")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(UserResponse.fromUser(newUser))
                        .build()
        );

    }


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserDetail(
            @Valid @PathVariable Long userId,
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) throws DataNotFoundException {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(error -> error.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Input is invalid !")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .data(errors)
                            .build()
            );
        }
        User updatedUser = userService.updateUserDetails(userId, userDTO);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Updated user successfully !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(UserResponse.fromUser(updatedUser))
                        .build()
        );

    }


    @PutMapping("/enable/{userId}/{isUnable}")
    public ResponseEntity<ResponseObject> unableOrEnable(
            @Valid @PathVariable Long userId,
            @Valid @PathVariable boolean isUnable
    ) throws DataNotFoundException {

        userService.unableOrEnabledUser(userId, isUnable);
        String message = isUnable ? "Successfully enabled user !!" : "Successfully unable user !!";

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );

    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request,
            BindingResult result
    ) throws DataNotFoundException {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Login failed")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }

        String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        String userAgent = request.getHeader("User-Agent");

        User userDetails = userService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(
                userDetails,
                token,
                userAgent.equalsIgnoreCase("mobile")
        );
        LoginResponse loginResponse = LoginResponse.builder()
                .message("Login successful")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Login successful")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(loginResponse)
                        .build()
        );

    }

    @PostMapping("/refreshToken/{refreshToken}")
    public ResponseEntity<ResponseObject> refreshToken(
            @PathVariable String refreshToken
    ) throws DataNotFoundException, ExpiredTokenException {
        User userDetails = userService.getUserDetailsFromRefreshToken(refreshToken);
        Token jwtToken = tokenService.refreshToken(refreshToken, userDetails);
        LoginResponse loginResponse = LoginResponse.builder()
                .message("Refresh token successful")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Refresh token successful")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(loginResponse)
                        .build()
        );
    }

    @PutMapping("/resetPassword/{userId}/{newPassword}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> resetPassword(
            @Valid @PathVariable("userId") Long userId,
            @Valid @PathVariable("newPassword") String newPassword
    ) throws DataNotFoundException {
        userService.resetPassword(userId, newPassword);
        return ResponseEntity.ok() .body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Reset password successful")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }


}
