package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordRequest;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordMapper;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicRecordService {
    private final MusicRecordRepository musicRecordRepository;
    private final UserService userService;

    public List<MusicRecordResponseShort> getAllMusicRecords(){
        List<MusicRecord> musicRecords = musicRecordRepository.findAll();
        return listToDtoShort(musicRecords);
    }

    public List<MusicRecordResponse> getFilteredMusicRecord(String title, String artist) {
        List<MusicRecord> filtered = new ArrayList<>();
        boolean titleIsEmpty = title == null || title.isBlank();
        boolean artistIsEmpty = artist == null || artist.isBlank();

        if (!titleIsEmpty && !artistIsEmpty) {
            filtered = musicRecordRepository.findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase(title, artist);
        } else if (!titleIsEmpty) {
            filtered = musicRecordRepository.findByTitleContainingIgnoreCase(title);
        } else if (!artistIsEmpty) {
            filtered = musicRecordRepository.findByArtistContainingIgnoreCase(artist);
        }
        return listToDto(filtered);
    }

    public List<MusicRecordResponse> getMusicRecordsByUserUsername(String username) {
        User user = userService.getByUsername(username);
        List<MusicRecord> listToDto = musicRecordRepository.findByUser(user);
        return listToDto (listToDto);
    }

    @PreAuthorize("isAuthenticated()")
    public MusicRecordResponse addMusicRecord(MusicRecordRequest musicRecordRequest, User user){
        MusicRecord musicRecord = MusicRecordMapper.toEntity(musicRecordRequest, user);
        MusicRecord saveMusicRecord = musicRecordRepository.save(musicRecord);
        return MusicRecordMapper.toDto(saveMusicRecord);
    }

    @PreAuthorize("isAuthenticated()")
    public MusicRecordResponse updateMusicRecord(Long id, MusicRecordRequest musicRecordRequest, User user){
        MusicRecord musicRecord = findMusicRecordOrThrow(id);
        assertUserIsOwner(musicRecord, user);
        musicRecord.setTitle(musicRecordRequest.title());
        musicRecord.setArtist(musicRecordRequest.artist());
        musicRecord.setMusicalGenre(musicRecordRequest.musicalGenre());
        musicRecord.setYear(musicRecordRequest.year());
        musicRecord.setImageUrl(musicRecordRequest.image());
        musicRecord.setSetlist(musicRecordRequest.setlist());

        MusicRecord updated = musicRecordRepository.save(musicRecord);
        return MusicRecordMapper.toDto(updated);
    }

    @PreAuthorize("isAuthenticated()")
    public String deleteMusicRecord(Long id, User user) {
        MusicRecord musicRecord = findMusicRecordOrThrow(id);
        assertUserIsOwnerOrAdmin(musicRecord, user);
        musicRecordRepository.delete(musicRecord);
        return "Music record with id " + id + " deleted successfully";
    }

    private MusicRecord findMusicRecordOrThrow(Long id) {
        return musicRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Music record", "id", id.toString()));
    }
    public void assertUserIsOwner(MusicRecord musicRecord, User user) {
        if (!musicRecord.getUser().getId().equals(user.getId()) ) {
            throw new AccessDeniedException("You are not authorized to modify or delete this music record.");
        }
    }

    public void assertUserIsOwnerOrAdmin(MusicRecord musicRecord, User user) {
        if (!musicRecord.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You are not authorized to modify or delete this music record.");
        }
    }

    private List<MusicRecordResponseShort> listToDtoShort(List<MusicRecord> musicRecords) {
        return musicRecords.stream()
                .map(MusicRecordMapper::toDtoShort)
                .toList();
    }

    private List<MusicRecordResponse> listToDto(List<MusicRecord> musicRecords) {
        return musicRecords.stream()
                .map(MusicRecordMapper::toDto)
                .toList();
    }
}
