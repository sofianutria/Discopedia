package com.example.discopedia.discopedia.security;

import com.example.discopedia.discopedia.security.jwt.JwtResponse;
import com.example.discopedia.discopedia.security.jwt.JwtService;
import com.example.discopedia.discopedia.users.UserService;
import com.example.discopedia.discopedia.users.dtos.UserLoginRequest;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@EnableMethodSecurity
@RestController
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register (@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        UserResponse userResponse = userService.addUser(userRegisterRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/register")
    public ResponseEntity<UserResponse> adminRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        UserResponse userResponse = userService.addAdmin(userRegisterRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@RequestBody @Valid UserLoginRequest userLoginRequest){
        JwtResponse jwtResponse = jwtService.loginAuthentication(userLoginRequest);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
