package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.users.dtos.UserMapper;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

}
