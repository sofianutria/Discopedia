package com.example.discopedia.discopedia.musicRecord;

import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.musicrecords.MusicRecordRepository;
import com.example.discopedia.discopedia.musicrecords.MusicRecordService;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordMapper;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordRequest;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserService;
import com.example.discopedia.discopedia.users.dtos.UserResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicRecordServiceTest {

    @Mock
    private MusicRecordRepository musicRecordRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MusicRecordService musicRecordService;

    private User user;
    private MusicRecord musicRecord;
    private MusicRecordRequest musicRecordRequest;
    private MusicRecordResponse musicRecordResponse;
    private MusicRecordResponseShort musicRecordResponseShort;
    private UserResponse userResponse;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User(1L, "sofia", "sofia@email.com", "encoded-password", Role.USER, new ArrayList<>(), new ArrayList<>());
        musicRecord = new MusicRecord(1L, "Happier Than Ever", "Billie Eilish", "Pop", 2021, "setlist", "image.url", user, new ArrayList<>());
        musicRecordResponse = new MusicRecordResponse(1L, "Happier Than Ever", "Billie Eilish", "Pop", 2021, "setlist", "image.url",new UserResponse(1L, "sofia", "sofia@email.com", "USER"));
        musicRecordRequest = new MusicRecordRequest("Happier Than Ever", "Billie Eilish", "Pop", 2021, "setlist", "image.url");
         userResponse = new UserResponse(1L, "sofia", "sofia@email.com", "USER");
        musicRecordResponse = MusicRecordMapper.toDto(musicRecord);
        musicRecordResponseShort = MusicRecordMapper.toDtoShort(musicRecord);

    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(musicRecordRepository, userService);
    }

    @Nested
    @DisplayName("GET MusicRecords")
    class GetMusicRecordsTests {

        @Test
        void getAllMusicRecords_whenRecordsExist_returnsList() {
            when(musicRecordRepository.findAll()).thenReturn(List.of(musicRecord));

            List<MusicRecordResponseShort> result = musicRecordService.getAllMusicRecords();
            assertEquals(List.of(musicRecordResponseShort), result);
            verify(musicRecordRepository, times(1)).findAll();
        }

        @Test
        void getAllMusicRecords_whenEmpty_returnsEmptyList() {
            when(musicRecordRepository.findAll()).thenReturn(List.of());

            List<MusicRecordResponseShort> result = musicRecordService.getAllMusicRecords();
            assertEquals(Collections.emptyList(), result);
            verify(musicRecordRepository, times(1)).findAll();
        }

        @Test
        void getFilteredMusicRecord_whenTitleAndArtistProvided_returnsList() {
            when(musicRecordRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase("happier", "billie"))
                    .thenReturn(List.of(musicRecord));

            List<MusicRecordResponse> result = musicRecordService.getFilteredMusicRecord("happier", "billie");
            assertEquals(List.of(musicRecordResponse), result);
            verify(musicRecordRepository, times(1))
                    .findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase("happier", "billie");
        }

        @Test
        void getFilteredMusicRecord_whenOnlyTitleProvided_returnsList() {
            when(musicRecordRepository.findByTitleContainingIgnoreCase("happier"))
                    .thenReturn(List.of(musicRecord));

            List<MusicRecordResponse> result = musicRecordService.getFilteredMusicRecord("happier", null);
            assertEquals(List.of(musicRecordResponse), result);
            verify(musicRecordRepository, times(1)).findByTitleContainingIgnoreCase("happier");
        }

        @Test
        void getFilteredMusicRecord_whenOnlyArtistProvided_returnsList() {
            when(musicRecordRepository.findByArtistContainingIgnoreCase("billie"))
                    .thenReturn(List.of(musicRecord));

            List<MusicRecordResponse> result = musicRecordService.getFilteredMusicRecord(null, "billie");
            assertEquals(List.of(musicRecordResponse), result);
            verify(musicRecordRepository, times(1)).findByArtistContainingIgnoreCase("billie");
        }

        @Test
        void getFilteredMusicRecord_whenNoParams_returnsEmptyList() {
            List<MusicRecordResponse> result = musicRecordService.getFilteredMusicRecord("", "");
            assertEquals(Collections.emptyList(), result);
        }

        @Test
        void getMusicRecordsByUserUsername_whenExists_returnsList() {
            when(userService.getByUsername("Sofía")).thenReturn(user);
            when(musicRecordRepository.findByUser(user)).thenReturn(List.of(musicRecord));

            List<MusicRecordResponse> result = musicRecordService.getMusicRecordsByUserUsername("Sofía");
            assertEquals(List.of(musicRecordResponse), result);
            verify(userService, times(1)).getByUsername("Sofía");
            verify(musicRecordRepository, times(1)).findByUser(user);
        }

        @Test
        void getMusicRecordsByUserUsername_whenEmpty_returnsEmptyList() {
            when(userService.getByUsername("Sofía")).thenReturn(user);
            when(musicRecordRepository.findByUser(user)).thenReturn(List.of());

            List<MusicRecordResponse> result = musicRecordService.getMusicRecordsByUserUsername("Sofía");
            assertEquals(Collections.emptyList(), result);
            verify(userService, times(1)).getByUsername("Sofía");
            verify(musicRecordRepository, times(1)).findByUser(user);
        }
    }

    @Nested
    @DisplayName("ADD MusicRecord")
    class AddMusicRecordTests {

        @Test
        void addMusicRecord_whenValidRequest_returnsDto() {
            when(musicRecordRepository.save(any(MusicRecord.class))).thenReturn(musicRecord);

            MusicRecordResponse result = musicRecordService.addMusicRecord(musicRecordRequest, user);
            assertEquals(musicRecordResponse, result);
            verify(musicRecordRepository, times(1)).save(any(MusicRecord.class));
        }
    }

    @Nested
    @DisplayName("UPDATE MusicRecord")
    class UpdateMusicRecordTests {

        @Test
        void updateMusicRecord_whenExistsAndOwned_returnsUpdatedDto() {
            when(musicRecordRepository.findById(1L)).thenReturn(Optional.of(musicRecord));
            when(musicRecordRepository.save(any(MusicRecord.class))).thenReturn(musicRecord);

            MusicRecordResponse result = musicRecordService.updateMusicRecord(1L, musicRecordRequest, user);
            assertEquals(musicRecordResponse, result);
            verify(musicRecordRepository, times(1)).findById(1L);
            verify(musicRecordRepository, times(1)).save(musicRecord);
        }

        @Test
        void updateMusicRecord_whenNotFound_throwsException() {
            when(musicRecordRepository.findById(1L)).thenReturn(Optional.empty());

            Exception ex = assertThrows(EntityNotFoundException.class,
                    () -> musicRecordService.updateMusicRecord(1L, musicRecordRequest, user));
            assertTrue(ex.getMessage().contains("Music record with id \"1\" not found"));
            verify(musicRecordRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("DELETE MusicRecord")
    class DeleteMusicRecordTests {

        @Test
        void deleteMusicRecord_whenExistsAndOwned_returnsSuccessMessage() {
            when(musicRecordRepository.findById(1L)).thenReturn(Optional.of(musicRecord));

            String result = musicRecordService.deleteMusicRecord(1L, user);
            assertEquals("Music record with id 1 deleted successfully", result);
            verify(musicRecordRepository, times(1)).findById(1L);
            verify(musicRecordRepository, times(1)).delete(musicRecord);
        }

        @Test
        void deleteMusicRecord_whenNotFound_throwsException() {
            when(musicRecordRepository.findById(1L)).thenReturn(Optional.empty());

            Exception ex = assertThrows(EntityNotFoundException.class,
                    () -> musicRecordService.deleteMusicRecord(1L, user));
            assertTrue(ex.getMessage().contains("Music record with id \"1\" not found"));
            verify(musicRecordRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("HELPERS assertUserRole")
    class AssertUserTests {

        @Test
        void assertUserIsOwner_whenOwner_doesNotThrow() {
            assertDoesNotThrow(() -> musicRecordService.assertUserIsOwner(musicRecord, user));
        }

        @Test
        void assertUserIsOwner_whenNotOwner_throwsAccessDenied() {
            User notOwner = new User(2L, "Other", "other@mail.com", "pwd", Role.USER, new ArrayList<>(), new ArrayList<>());
            AccessDeniedException ex = assertThrows(AccessDeniedException.class,
                    () -> musicRecordService.assertUserIsOwner(musicRecord, notOwner));
            assertEquals("You are not authorized to modify or delete this music record.", ex.getMessage());
        }

        @Test
        void assertUserIsOwnerOrAdmin_whenOwner_doesNotThrow() {
            assertDoesNotThrow(() -> musicRecordService.assertUserIsOwnerOrAdmin(musicRecord, user));
        }

        @Test
        void assertUserIsOwnerOrAdmin_whenAdmin_doesNotThrow() {
            User admin = new User(2L, "Admin", "admin@mail.com", "pwd", Role.ADMIN, new ArrayList<>(), new ArrayList<>());
            assertDoesNotThrow(() -> musicRecordService.assertUserIsOwnerOrAdmin(musicRecord, admin));
        }

        @Test
        void assertUserIsOwnerOrAdmin_whenNotOwnerNorAdmin_throwsAccessDenied() {
            User stranger = new User(3L, "Stranger", "str@mail.com", "pwd", Role.USER, new ArrayList<>(), new ArrayList<>());
            AccessDeniedException ex = assertThrows(AccessDeniedException.class,
                    () -> musicRecordService.assertUserIsOwnerOrAdmin(musicRecord, stranger));
            assertEquals("You are not authorized to modify or delete this music record.", ex.getMessage());
        }
    }
}

