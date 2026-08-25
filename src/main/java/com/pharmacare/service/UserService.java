package com.pharmacare.service;

import com.pharmacare.dto.request.RegisterUserDto;
import com.pharmacare.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void deleteUser(Long id);
    User registerUser(RegisterUserDto dto);
    User updateUser(Long id, RegisterUserDto dto);
}