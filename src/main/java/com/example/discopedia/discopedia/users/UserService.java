package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.users.dtos.UserMapper;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

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

    private UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString()));
        return UserMapper.toDto(user);

    }
}
