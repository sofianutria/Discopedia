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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private String asJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }

    @BeforeEach
    void setUp() {
        User user = new User(1L, "sofia", "sofia@email.com", "encoded-password", Role.ADMIN, new ArrayList<MusicRecord>(), new ArrayList<Review>());
        User userNotAdmin = new User(1L, "santos", "santos@email.com", "encoded-password", Role.USER, new ArrayList<MusicRecord>(), new ArrayList<Review>());
        userAdmin = new CustomUserDetail(user);
        userUser = new CustomUserDetail(userNotAdmin);
    }

    @Nested
    @DisplayName("REGISTER USER-POST /register")
    class RegisterUserTests{

        @Test
        @DisplayName("Shoul register a user and return 201 created")
        void testRegisterUser() throws Exception{
            UserRegisterRequest request = new UserRegisterRequest("john", "john@gmail.com", "LOL?PASSword23");
            UserResponse userResponse = new UserResponse(1L, "john", "john@gmail.com", "ROLE_USER");
            given(userService.addUser(request)).willReturn(userResponse);
            String expectedJson = asJsonString(userResponse);
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expectedJson));
            verify(userService, times(1)).addUser(request);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when password is missing during registration")
        void testRegisterWithMissingPassword () throws Exception {
            UserRegisterRequest invalidRequest = new UserRegisterRequest("john", "john@gmail.com", "");
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("Should return 400 Bad Request when username is missing during registration")
        void testRegisterWithMissingUsername() throws Exception {
            UserRegisterRequest invalidRequest = new UserRegisterRequest("", "john@gmail.com", "password");
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when register request body is empty")
        void testRegisterWithEmptyBody () throws Exception {
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }
}
