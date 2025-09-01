package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.users.dtos.UserMapper;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserByIdAdmin(Long id){
        return getUserById(id);
    }
    @PreAuthorize("isAuthenticated()")
    public UserResponse getOwnUser(Long id){return getUserById(id);}

    private UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User", "id", id.toString()));
        return UserMapper.toDto(user);
    }

    public UserResponse addUser(UserRegisterRequest request){return addUserByRole(request,Role.USER);}

    private UserResponse addUserByRole(UserRegisterRequest request, Role role) {
    }
}
