package com.example.discopedia.discopedia.users;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.reviews.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<MusicRecord> musicRecords;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
