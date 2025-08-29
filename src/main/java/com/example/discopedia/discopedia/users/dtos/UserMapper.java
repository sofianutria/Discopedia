package com.example.discopedia.discopedia.users.dtos;

import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;

public class UserMapper {

    public static User toEntity(UserRegisterRequest dto, Role role){
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .role(role)
                .build();
    }
}
