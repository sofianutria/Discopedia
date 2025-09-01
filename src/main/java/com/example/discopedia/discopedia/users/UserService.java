package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.users.dtos.UserMapper;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
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

    public User getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new EntityNotFoundException(User.class.getSimpleName(),"username", username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserResponse addUser(UserRegisterRequest request){
        return addUserByRole(request, Role.USER);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse addAdmin(UserRegisterRequest request){
        return addUserByRole(request, Role.ADMIN);
    }

    private UserResponse addUserByRole(UserRegisterRequest request, Role role) {
    }
}
