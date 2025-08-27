package com.example.discopedia.discopedia.musicrecords;

import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "music_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(name="musical_genre", nullable=false)
    private String musicalGenre;

    @Column(nullable = false)
    private int year;

    @Column (name="image_url", nullable=false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn (name="user_id")
    private User user;

    @OneToMany(mappedBy="musicRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
