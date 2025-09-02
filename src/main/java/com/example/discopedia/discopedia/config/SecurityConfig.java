package com.example.discopedia.discopedia.config;

import com.example.discopedia.discopedia.exceptions.ErrorResponse;
import com.example.discopedia.discopedia.security.jwt.JwtAuthFilter;
import com.example.discopedia.discopedia.security.jwt.JwtService;
import com.example.discopedia.discopedia.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final UserService userService;
    @Value ("${port.front}")
    private String frontPort;
    public SecurityConfig(UserService userService){this.userService=userService;}

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter (JwtService jwtService){return new JwtAuthFilter(jwtService, userService)}

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return (request, response, exception) ->{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.UNAUTHORIZED, "Unauthorized: " + exception.getMessage(), request);
            objectMapper().writeValue(response.getWriter(),error);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(HttpMethod.GET, "/cd").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
