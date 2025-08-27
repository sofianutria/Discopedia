package com.example.discopedia.discopedia.reviews;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;


}
