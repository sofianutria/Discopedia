package com.example.discopedia.discopedia.musicrecords.dtos;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.users.User;

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
}
