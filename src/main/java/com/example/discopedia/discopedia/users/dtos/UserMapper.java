package com.example.discopedia.discopedia.users.dtos;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
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

    public static UserResponse toDto(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    public static UserResponseShort toDtoShort (User user){
        return new UserResponseShort(user.getId(), user.getUsername());
    }
}
