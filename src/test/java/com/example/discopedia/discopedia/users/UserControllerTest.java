package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.musicrecords.MusicRecordService;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private MusicRecordService musicRecordService;
    @Autowired
    private ObjectMapper objectMapper;

    private UserRegisterRequest userRegisterRequest;
    private UserRegisterRequest invaliduserRegisterRequest;
    private UserResponse userResponse;
    private CustomUserDetail customUserDetail;
    private User user;
    private MusicRecordResponse musicRecordResponse;

    @BeforeEach
    void setUp(){
        musicRecordResponse = new MusicRecordResponse(1L, "21", "Adele", "Pop", 2011, "1. Rolling in the Deep", "image.url",new UserResponse(1L, "sofia", "sofia@email.com", "USER"));
        user = new User(1L, "sofia", "sofia@email.com", "encoded-password", Role.USER, new ArrayList<MusicRecord>(), new ArrayList<Review>());
        customUserDetail = new CustomUserDetail(user);
        userRegisterRequest = new UserRegisterRequest("sofia", "sofia@email.com", "Password1!.");
        userResponse = new UserResponse(1L, "sofia", "sofia@email.com", "USER");
        invaliduserRegisterRequest = new UserRegisterRequest("sofia", "sofia@email.com", "Password1!.");
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
}
