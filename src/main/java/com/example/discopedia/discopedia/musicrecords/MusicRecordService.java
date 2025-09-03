package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordMapper;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
