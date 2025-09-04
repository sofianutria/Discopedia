package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.musicrecords.MusicRecordService;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    private UserRegisterRequest invalidUserRegisterRequest;
    private UserResponse userResponse;
    private CustomUserDetail customUserDetail;
    private User user;
    private MusicRecordResponse musicRecordResponse;

    @BeforeEach
    void setUp(){
        musicRecordResponse = new MusicRecordResponse(1L, "21", "Adele", "Pop", 2011, "1. Rolling in the Deep", "image.url",new UserResponse(1L, "sofia", "sofia@email.com", "USER"));
        user = new User(1L, "sofia", "sofia@email.com", "encoded-password", Role.USER, new ArrayList<>(), new ArrayList<>());
        customUserDetail = new CustomUserDetail(user);
        userRegisterRequest = new UserRegisterRequest("sofia", "sofia@email.com", "Password1!.");
        userResponse = new UserResponse(1L, "sofia", "sofia@email.com", "USER");
        invalidUserRegisterRequest = new UserRegisterRequest("so", "sof@email.com", "pssword");
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(userService);
    }
    private String asJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }

    @Nested
    @DisplayName("GET /users")
    class GetUsersTests{
        @Test
        void geMyUser_withAuthentication_returnsUser() throws Exception {
            Long id = user.getId();
            given(userService.getOwnUser(eq(id))).willReturn(userResponse);

            mockMvc.perform(get("/users/me")
                            .with(user(customUserDetail)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(userResponse)));
            verify(userService, times(1)).getOwnUser(eq(id));
        }

        @Test
        void getMyUser_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }

        @Test
        void geMyMusicRecords_withAuthentication_returnsMusicRecordsList() throws Exception {
            given(musicRecordService.getMusicRecordsByUserUsername(customUserDetail.getUser().getUsername())).willReturn(List.of(musicRecordResponse));
            mockMvc.perform(get("/users/me/cd")
                            .with(user(customUserDetail)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of(musicRecordResponse))));
            verify(musicRecordService, times (1)).getMusicRecordsByUserUsername(eq(customUserDetail.getUser().getUsername()));
        }
        @Test
        void getMyMusicRecords_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(get("/users/me/cd"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me/cd"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }
    }

    @Nested
    @DisplayName("PUT /users")
    class UpdateUserTests {

        @Test
        void updateUser_withAuthentication_returnsUser() throws Exception {
            Long id = 1L;
            given(userService.updateOwnUser(eq(id), eq(userRegisterRequest))).willReturn(userResponse);
            mockMvc.perform(put("/users/me")
                            .with(user(customUserDetail))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(userRegisterRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(userResponse)));
            verify(userService, times(1)).updateOwnUser(eq(id), eq(userRegisterRequest));
        }

        @Test
        void updateUser_whenInvalidUsernameAndPassword_returns400() throws Exception {
            mockMvc.perform(put("/users/me", 1)
                            .with(user(customUserDetail))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(invalidUserRegisterRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message.username").value("Username must be between 3 and 50 characters"))
                    .andExpect(jsonPath("$.message.password").value("Password must contain a minimum of 8 characters and a max of 50 characters, including a number, one uppercase letter, one lowercase letter and one special character"));
        }

        @Test
        void updateUser_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(put("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(userRegisterRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }
    }

    @Nested
    @DisplayName("DELETE /users")
    class DeleteUsersTests {
        @Test
        void deleteUser_withAuthentication_returnsMessage() throws Exception {
            Long id = 1L;
            String message = "User deleted successfully";
            given(userService.deleteOwnUser(eq(id))).willReturn(message);
            mockMvc.perform(delete("/users/me")
                            .with(user(customUserDetail)))
                    .andExpect(content().string(message));
            verify(userService, times(1)).deleteOwnUser(eq(id));
        }

        @Test
        void deleteUser_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(delete("/users/me"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.length()").value(5))
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }
    }
}
