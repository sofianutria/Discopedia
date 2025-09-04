package com.example.discopedia.discopedia.reviews;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserAndMusicRecord(User user, MusicRecord musicRecord);
}
