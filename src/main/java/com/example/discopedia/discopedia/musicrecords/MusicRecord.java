package com.example.discopedia.discopedia.musicrecords;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable=false)
    private String musicalGenre;

    @Column(nullable = false)
    private int year;
}
