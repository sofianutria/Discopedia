package com.example.discopedia.discopedia.security;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.security.jwt.JwtService;
import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserService;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    private UserDetails userAdmin;
    private UserDetails userUser;

    private String asJsonString(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception){
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }
    @BeforeEach
    void setUp(){
            User user = new User(1L, "sofia", "sofia@email.com", "encoded-password", Role.ADMIN, new ArrayList<MusicRecord>(), new ArrayList<Review>());
            User userNotAdmin = new User(1L, "santos", "santos@email.com", "encoded-password", Role.USER, new ArrayList<MusicRecord>(), new ArrayList<Review>());
            userAdmin = new CustomUserDetail(user);
            userUser = new CustomUserDetail(userNotAdmin);
        }
    }
