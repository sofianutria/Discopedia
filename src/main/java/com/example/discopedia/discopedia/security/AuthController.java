package com.example.discopedia.discopedia.security;

import com.example.discopedia.discopedia.security.jwt.JwtService;
import com.example.discopedia.discopedia.users.UserService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
}
