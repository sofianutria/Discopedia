package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    private User user;
    private UserRegisterRequest userRegisterRequest;
    private UserResponse userResponse;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        user = new User(1L,"sofia","sofia@email.com","encoded-password",Role.USER, new ArrayList<MusicRecord>(), new ArrayList<Review>());
        userResponse = new UserResponse(1L, "sofia@email.com","encoded-password", "USER");
        userRegisterRequest = new UserRegisterRequest("sofia", "sofia@email.com","Password1!.");
    }

    @AfterEach
    void afterTest(){verifyNoMoreInteractions(userRepository);}
}
