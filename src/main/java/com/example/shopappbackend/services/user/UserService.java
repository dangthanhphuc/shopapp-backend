package com.example.shopappbackend.services.user;

import com.example.shopappbackend.dtos.UserDTO;
import com.example.shopappbackend.entities.Role;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.RoleRepository;
import com.example.shopappbackend.repositories.UserRepository;
import com.example.shopappbackend.services.role.IRoleService;
import com.example.shopappbackend.services.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    private final ModelMapper modelMapper;

    @Override
    public List<User> getUsers() throws DataNotFoundException {
        List<User> users = userRepository.findAll().stream().filter(user -> !user.isDeleted()).toList();
        if (users.isEmpty()){
            throw new DataNotFoundException("User list is empty");
        }
        return users;
    }

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {

        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new DataIntegrityViolationException("User gmail already exists");
        }

        User newUser = new User();
        modelMapper.map(userDTO, newUser);

        // Lấy Role USER làm mặc định
        Role role = roleService.getRoleById(2L);
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    @Override
    public User updateUserDetails(Long userId, UserDTO userDTO) throws DataNotFoundException {
        User existingUser = getUserById(userId);
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new DataIntegrityViolationException("User gmail already exists");
        }
        modelMapper.map(userDTO ,existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public void unableOrEnabledUser(Long userId, boolean isUnable) throws DataNotFoundException {
        User existingUser = getUserById(userId);
        existingUser.setDeleted(isUnable);
        userRepository.save(existingUser);
    }

    @Override
    public User getUser(Long userId) throws DataNotFoundException {
        return getUserById(userId);
    }

    public User getUserById(long id) throws DataNotFoundException {
        return userRepository.findById(id).stream()
                .filter(user -> !user.isDeleted())
                .findFirst()
                .orElseThrow(
                        () -> new DataNotFoundException("Cannot find product wit id: " + id)
                );
    }

}
