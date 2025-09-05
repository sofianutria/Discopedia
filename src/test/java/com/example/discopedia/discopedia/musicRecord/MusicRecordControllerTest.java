package com.example.discopedia.discopedia.musicRecord;

import com.example.discopedia.discopedia.musicrecords.MusicRecordService;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordRequest;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserService;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MusicRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MusicRecordService musicRecordService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDetails testUserDetails;
    private UserDetails userDetailsNotOwner;
    private MusicRecordRequest musicRecordRequest;
    private MusicRecordRequest invalidMusicRecordRequest;
    private MusicRecordResponse musicRecordResponse;
    private MusicRecordResponseShort musicRecordResponseShort;

    @BeforeEach
    void setUp() {
        user = new User(1L, "sofia", "sofia@email.com", "encoded-password", Role.USER, new ArrayList<>(), new ArrayList<>());
        testUserDetails = new CustomUserDetail(user);

        User notOwner = new User(2L, "other", "other@email.com", "pwd", Role.USER, new ArrayList<>(), new ArrayList<>());
        userDetailsNotOwner = new CustomUserDetail(notOwner);

        musicRecordRequest = new MusicRecordRequest("Happier Than Ever", "Billie Eilish", "Pop", 2021, "setlist", "https://example.com/image.jpg");
        invalidMusicRecordRequest = new MusicRecordRequest("Ha", "Bi", "P", 2021, "", "");

        UserResponse userResponse = new UserResponse(1L, "sofia", "sofia@email.com", "USER");
        musicRecordResponse = new MusicRecordResponse(1L, "Happier Than Ever", "Billie Eilish", "Pop", 2021, "setlist", "https://example.com/image.jpg", userResponse);
        musicRecordResponseShort = new MusicRecordResponseShort("Happier Than Ever","Billie Eilish", "Pop");
    }

    @AfterEach
    void afterTest() {
        verifyNoMoreInteractions(musicRecordService, userService);
    }

    private String asJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string", exception);
        }
    }

    private static final Map<HttpMethod, Function<String, MockHttpServletRequestBuilder>> REQUEST_BUILDERS = Map.of(
            HttpMethod.GET, url -> get(url),
            HttpMethod.POST, url -> post(url),
            HttpMethod.PUT, url -> put(url),
            HttpMethod.DELETE, url -> delete(url)
    );

    private ResultActions performRequest(HttpMethod method, String url, Object body, UserDetails user) throws Exception {
        Function<String, MockHttpServletRequestBuilder> builderFunction = REQUEST_BUILDERS.get(method);
        if (builderFunction == null) throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        MockHttpServletRequestBuilder builder = builderFunction.apply(url);
        builder.contentType(MediaType.APPLICATION_JSON);
        if (body != null) builder.content(asJsonString(body));
        if (user != null) builder.with(user(user));
        return mockMvc.perform(builder);
    }

    @Nested
    @DisplayName("GET /cd")
    class GetMusicRecordsTests {

        @Test
        void getAllMusicRecords_returnsList() throws Exception {
            given(musicRecordService.getAllMusicRecords()).willReturn(List.of(musicRecordResponseShort));

            mockMvc.perform(get("/cd"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of(musicRecordResponseShort))));

            verify(musicRecordService, times(1)).getAllMusicRecords();
        }

        @Test
        void getAllMusicRecords_returnsEmptyList() throws Exception {
            given(musicRecordService.getAllMusicRecords()).willReturn(List.of());

            mockMvc.perform(get("/cd"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of())));

            verify(musicRecordService, times(1)).getAllMusicRecords();
        }

        @Test
        void getMusicRecordsByUser_whenUserExists_returnsList() throws Exception {
            given(musicRecordService.getMusicRecordsByUserUsername(user.getUsername())).willReturn(List.of(musicRecordResponse));

            mockMvc.perform(get("/cd/user?username=" + user.getUsername()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of(musicRecordResponse))));

            verify(musicRecordService, times(1)).getMusicRecordsByUserUsername(user.getUsername());
        }

        @Test
        void getMusicRecordsByUser_whenUserDoesNotExist_returns404() throws Exception {
            String username = "notfound";
            given(musicRecordService.getMusicRecordsByUserUsername(username))
                    .willThrow(new EntityNotFoundException("User", "username", username));

            mockMvc.perform(get("/cd/user?username=" + username))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value("User with username \"" + username + "\" not found"));

            verify(musicRecordService, times(1)).getMusicRecordsByUserUsername(username);
        }
    }

    @Nested
    @DisplayName("POST /cd")
    class AddMusicRecordTests {

        @Test
        void addMusicRecord_whenValid_returnsCreatedRecord() throws Exception {
            given(musicRecordService.addMusicRecord(musicRecordRequest, user)).willReturn(musicRecordResponse);

            performRequest(POST, "/cd", musicRecordRequest, testUserDetails)
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(musicRecordResponse)));

            verify(musicRecordService, times(1)).addMusicRecord(musicRecordRequest, user);
        }

        @Test
        void addMusicRecord_whenInvalid_returns400() throws Exception {
            performRequest(POST, "/cd", invalidMusicRecordRequest, testUserDetails)
                    .andExpect(status().isBadRequest());
        }

        @Test
        void addMusicRecord_whenUnauthorized_returns401() throws Exception {
            performRequest(POST, "/cd", musicRecordRequest, null)
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("PUT /cd/{id}")
    class UpdateMusicRecordTests {

        @Test
        void updateMusicRecord_whenValid_returnsUpdated() throws Exception {
            Long id = 1L;
            given(musicRecordService.updateMusicRecord(eq(id), eq(musicRecordRequest), any(User.class)))
                    .willReturn(musicRecordResponse);

            performRequest(PUT, "/cd/" + id, musicRecordRequest, testUserDetails)
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(musicRecordResponse)));

            verify(musicRecordService, times(1)).updateMusicRecord(eq(id), eq(musicRecordRequest), any(User.class));
        }

        @Test
        void updateMusicRecord_whenUnauthorized_returns401() throws Exception {
            performRequest(PUT, "/cd/1", musicRecordRequest, null)
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void updateMusicRecord_whenUserNotOwner_returns403() throws Exception {
            Long id = 1L;
            given(musicRecordService.updateMusicRecord(eq(id), eq(musicRecordRequest), any(User.class)))
                    .willThrow(new AccessDeniedException("You are not authorized to modify or delete this music record."));

            performRequest(PUT, "/cd/" + id, musicRecordRequest, userDetailsNotOwner)
                    .andExpect(status().isForbidden());

            verify(musicRecordService, times(1)).updateMusicRecord(eq(id), eq(musicRecordRequest), any(User.class));
        }
    }

    @Nested
    @DisplayName("DELETE /cd/{id}")
    class DeleteMusicRecordTests {

        @Test
        void deleteMusicRecord_whenExists_returnsMessage() throws Exception {
            Long id = 1L;
            String msg = "Music record with id " + id + " deleted successfully";
            given(musicRecordService.deleteMusicRecord(id, user)).willReturn(msg);

            performRequest(DELETE, "/cd/" + id, null, testUserDetails)
                    .andExpect(status().isOk())
                    .andExpect(content().string(msg));

            verify(musicRecordService, times(1)).deleteMusicRecord(id, user);
        }

        @Test
        void deleteMusicRecord_whenDoesNotExist_returns404() throws Exception {
            Long id = 1L;
            given(musicRecordService.deleteMusicRecord(id, user))
                    .willThrow(new EntityNotFoundException("MusicRecord", "id", id.toString()));

            performRequest(DELETE, "/cd/" + id, null, testUserDetails)
                    .andExpect(status().isNotFound());

            verify(musicRecordService, times(1)).deleteMusicRecord(id, user);
        }

        @Test
        void deleteMusicRecord_whenUnauthorized_returns401() throws Exception {
            performRequest(DELETE, "/cd/1", null, null)
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteMusicRecord_whenUserNotOwner_returns403() throws Exception {
            Long id = 1L;
            given(musicRecordService.deleteMusicRecord(eq(id), any(User.class)))
                    .willThrow(new AccessDeniedException("You are not authorized to modify or delete this music record."));

            performRequest(DELETE, "/cd/" + id, null, userDetailsNotOwner)
                    .andExpect(status().isForbidden());

            verify(musicRecordService, times(1)).deleteMusicRecord(eq(id), any(User.class));
        }
    }
}

