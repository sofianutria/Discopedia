package com.example.discopedia.discopedia.models;

import com.example.discopedia.discopedia.users.Role;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "users")
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
    private List<Record> records;

}
