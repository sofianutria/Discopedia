package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.exceptions.EntityAlreadyExistsException;
import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.security.CustomUserDetail;
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
        return userRepository.findByUsername(username)
                .map(user-> new CustomUserDetail(user))
                .orElseThrow(()-> new EntityNotFoundException("User", "username", username.toString()));
    }

    public UserResponse addUser(UserRegisterRequest request){
        return addUserByRole(request, Role.USER);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse addAdmin(UserRegisterRequest request){
        return addUserByRole(request, Role.ADMIN);
    }

    private UserResponse addUserByRole(UserRegisterRequest request, Role role) {
        if (userRepository.existsByUsername(request.username())){
            throw new EntityAlreadyExistsException(User.class.getSimpleName(),"username", request.username());
        }
        if (userRepository.existsByEmail(request.email())){
            throw new EntityAlreadyExistsException(User.class.getSimpleName(), "email", request.email());
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = UserMapper.toEntity(request, role);
        user.setPassword(encodedPassword);
        User savedUser=userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }
}
