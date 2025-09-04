package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordRequest;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordMapper;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserService;
import lombok.RequiredArgsConstructor;
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

    public List<MusicRecordResponseShort> getAllDestinations(){
        List<MusicRecord> musicRecords = musicRecordRepository.findAll();
        return listToDtoShort(musicRecords);
    }

    public List<MusicRecordResponseShort> getAllMusicRecordsUser(String username){
        List<MusicRecord> musicRecords = musicRecordRepository.findAll();
        musicRecords.sort(Comparator.comparing(musicRecord -> musicRecord.getUser() != null && username.equals(musicRecord.getUser().getUsername()) ? 0 : 1));
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

    @PreAuthorize("isAuthenticated()")
    public MusicRecordResponse addMusicRecord(MusicRecordRequest musicRecordRequest, User user){
        MusicRecord musicRecord = MusicRecordMapper.toEntity(musicRecordRequest, user);
        MusicRecord saveMusicRecord = musicRecordRepository.save(musicRecord);
        return MusicRecordMapper.toDto(saveMusicRecord);
    }

    private List<MusicRecordResponseShort> listToDtoShort(List<MusicRecord> musicRecords) {
        return musicRecords.stream()
                .map(MusicRecordMapper::toDtoShort)
                .toList();
    }

    public List<MusicRecordResponse> getCdsByUserUsername(String username) {
        User user = userService.getByUsername(username);
        List<MusicRecord> listToDto = musicRecordRepository.findByUser(user);
        return listToDto (listToDto);
    }

    private List<MusicRecordResponse> listToDto(List<MusicRecord> musicRecords) {
        return musicRecords.stream()
                .map(MusicRecordMapper::toDto)
                .toList();
    }
}
