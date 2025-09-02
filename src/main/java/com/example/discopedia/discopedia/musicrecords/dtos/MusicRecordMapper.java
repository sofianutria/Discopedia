package com.example.discopedia.discopedia.musicrecords.dtos;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.dtos.UserMapper;
import com.example.discopedia.discopedia.users.dtos.UserResponse;

public class MusicRecordMapper {
    public static MusicRecord toEntity(MusicRecordRequest dto, User user){
        return MusicRecord.builder()
                .title(dto.title())
                .artist(dto.artist())
                .musicalGenre(dto.musicalGenre())
                .year(dto.year())
                .setlist(dto.setlist())
                .imageUrl(dto.image())
                .build();
    }
    public static MusicRecordResponse toDto(MusicRecord musicRecord){
        UserResponse userDto = UserMapper.toDto(musicRecord.getUser());
        return new MusicRecordResponse(musicRecord.getId(), musicRecord.getTitle(), musicRecord.getArtist(), musicRecord.getMusicalGenre(), musicRecord.getYear(), musicRecord.getSetlist(), musicRecord.getImageUrl(), userDto);
    }
}
