package com.example.discopedia.discopedia.config;

import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@Configuration
public class DefaultAdminInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner createDefaultAdmin (UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        return args -> {
            if(!userRepository.existsByEmail(adminEmail) && !userRepository.existsByUsername(adminUsername)){
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ADMIN);
                admin.setMusicRecords(Collections.emptyList());

                userRepository.save(admin);
                System.out.println("Default admin user created: " + adminEmail);
            } else{
                System.out.println("Admin user already exists: " + adminEmail);
            }
        };
    }
}
