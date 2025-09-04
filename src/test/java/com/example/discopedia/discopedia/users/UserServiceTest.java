package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.exceptions.EntityAlreadyExistsException;
import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.users.dtos.UserRegisterRequest;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Nested
    @DisplayName("GET users")
    class GetUserTests{
        @Test
        void getAllUsers_whenUsersExist_returnsListOfUsersResponse(){
            when (userRepository.findAll()).thenReturn(List.of(user));
            List<UserResponse> result = userService.getAllUsers();
            assertEquals(List.of(userResponse), result);
            verify(userRepository, times(1)).findAll();
        }

        @Test
        void getAllUsers_whenNoUsers_returnsEmptyList() {
            when(userRepository.findAll()).thenReturn(List.of());
            List<UserResponse> result = userService.getAllUsers();
            assertTrue(result.isEmpty());
            verify(userRepository, times(1)).findAll();
        }

        @Test
        void getUserByIdAdmin_whenUserExists_returnsUserResponse() {
            Long id = 1L;
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            UserResponse result = userService.getUserByIdAdmin(id);
            assertEquals(userResponse, result);
            verify(userRepository, times(1)).findById(id);
        }
        @Test
        void getUserByIdAdmin_whenUserDoesNotExist_throwsException() {
            Long id = 1L;
            when(userRepository.findById(id)).thenReturn(Optional.empty());
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> userService.getUserByIdAdmin(id));
            assertEquals("User with id \"" + id + "\" not found", exception.getMessage());
            verify(userRepository, times(1)).findById(id);
        }
        @Test
        void getOwnUser_whenUserExists_returnsUserResponse() {
            Long id = 1L;
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            UserResponse result = userService.getOwnUser(id);
            assertEquals(userResponse, result);
            verify(userRepository, times(1)).findById(id);
        }
        @Test
        void getOwnUser_whenUserDoesNotExist_throwsException() {
            Long id = 1L;
            when(userRepository.findById(id)).thenReturn(Optional.empty());
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> userService.getOwnUser(id));
            assertEquals("User with id \"" + id + "\" not found", exception.getMessage());
            verify(userRepository, times(1)).findById(id);
        }
        @Test
        void getByUsername_whenUserExists_returnsUser() {
            String username = "sofia";
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            User result = userService.getByUsername(username);
            assertEquals(user, result);
            verify(userRepository, times(1)).findByUsername(username);
        }
        @Test
        void getByUsername_whenUserDoesNotExist_throwsException() {
            String username = "Lara";
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> userService.getByUsername(username));
            assertEquals("User with username \"" + username + "\" not found", exception.getMessage());
            verify(userRepository, times(1)).findByUsername(username);
        }
    }

    @Nested
    @DisplayName("POST users")
    class AddUserTest{
        @Test
        void addUser_whenUserIsNew_returnsUserResponse() {
            when(userRepository.existsByUsername(userRegisterRequest.username())).thenReturn(false);
            when(userRepository.existsByEmail(userRegisterRequest.email())).thenReturn(false);
            when(passwordEncoder.encode(any())).thenReturn("encoded-password");
            when(userRepository.save(any(User.class))).thenReturn(user);
            UserResponse result = userService.addUser(userRegisterRequest);
            assertEquals(userResponse, result);
            verify(userRepository, times(1)).existsByUsername(userRegisterRequest.username());
            verify(userRepository, times(1)).existsByEmail(userRegisterRequest.email());
            verify(userRepository, times(1)).save(any(User.class));
        }
        @Test
        void addUser_whenUsernameAlreadyExists_throwsException() {
            when(userRepository.existsByUsername(userRegisterRequest.username())).thenReturn(true);
            EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                    () -> userService.addUser(userRegisterRequest));
            assertEquals("User with username \"" + userRegisterRequest.username() + "\" already exists", exception.getMessage());
            verify(userRepository, times(1)).existsByUsername(userRegisterRequest.username());
        }
    }
}
