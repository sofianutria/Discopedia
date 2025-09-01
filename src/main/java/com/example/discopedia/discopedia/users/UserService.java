package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.dtos.UserMapper;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user-> new CustomUserDetail(user))
                .orElseThrow(()-> new EntityNotFoundException("User", "username", username.toString()));
    }
}
