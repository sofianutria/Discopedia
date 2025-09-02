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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final UserService userService;
    public SecurityConfig(UserService userService){this.userService=userService;}

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter (JwtService jwtService){return new JwtAuthFilter(jwtService, userService);}

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers( "/register", "/login").permitAll()
                        .requestMatchers("/users", "/users/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/cd/auth").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cd", "/cd/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cd").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/cd/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/cd/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
