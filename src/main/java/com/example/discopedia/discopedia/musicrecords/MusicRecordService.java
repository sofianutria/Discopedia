package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordMapper;
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
