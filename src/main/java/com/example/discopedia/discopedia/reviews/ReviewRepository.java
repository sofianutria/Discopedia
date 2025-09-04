package com.example.discopedia.discopedia.reviews;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserAndMusicRecord(User user, MusicRecord musicRecord);
    List<Review> findByMusicRecord(MusicRecord musicRecord);
}
