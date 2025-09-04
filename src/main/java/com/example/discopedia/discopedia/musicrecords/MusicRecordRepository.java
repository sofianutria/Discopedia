package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRecordRepository extends JpaRepository <MusicRecord, Long> {
    List<MusicRecord> findByUser (User user);
    List<MusicRecord> findByTitleContainingIgnoreCase (String title);
    List<MusicRecord> findByArtistContainingIgnoreCase (String artist);
    List<MusicRecord> findByTitleContainingIgnoreCaseAndArtistContainingIgnoreCase (String title, String country);
}
