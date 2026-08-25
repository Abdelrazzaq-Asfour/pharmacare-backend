package com.pharmacare.mapper;

import com.pharmacare.model.Role;
import com.pharmacare.model.User;
import com.pharmacare.dto.response.JwtAuthResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Handles user domain mapping for authentication and authorization contexts.
 */
@Component
public class UserMapper {

    public JwtAuthResponse toJwtResponse(User user, String accessToken) {
        if (user == null) {
            return null;
        }

        var rolesSet = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        return new JwtAuthResponse(accessToken, user.getUsername(), rolesSet);
    }
}