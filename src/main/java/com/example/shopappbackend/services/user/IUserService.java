package com.example.shopappbackend.services.user;

import com.example.shopappbackend.dtos.UserDTO;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.exceptions.InvalidPasswordException;

import java.util.List;

public interface IUserService {
    List<User> getUsers() throws DataNotFoundException;
    User createUser(UserDTO userDTO) throws DataNotFoundException, InvalidPasswordException;
    User updateUserDetails(Long userId ,UserDTO userDTO) throws DataNotFoundException;
    void unableOrEnabledUser(Long userId, boolean isUnable) throws DataNotFoundException;
    User getUser(Long userId) throws DataNotFoundException;
    String login(String email, String password) throws DataNotFoundException;
    User getUserDetailsFromToken(String token) throws DataNotFoundException;
    User getUserDetailsFromRefreshToken(String refreshToken) throws DataNotFoundException;
    void resetPassword(Long userId ,String newPassword) throws DataNotFoundException;
}
