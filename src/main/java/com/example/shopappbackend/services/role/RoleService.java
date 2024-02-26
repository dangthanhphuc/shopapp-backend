package com.example.shopappbackend.services.role;

import com.example.shopappbackend.entities.Role;
import com.example.shopappbackend.entities.User;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(long id) throws DataNotFoundException {
        return roleRepository.findById(id).stream()
                .filter(role -> !role.isDeleted())
                .findFirst()
                .orElseThrow(
                        () -> new DataNotFoundException("Cannot find role wit id: " + id)
                );
    }
}
