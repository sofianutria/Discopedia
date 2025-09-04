package com.example.discopedia.discopedia.config;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.musicrecords.MusicRecordRepository;
import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.reviews.ReviewRepository;
import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class DataSeeder {
    @Bean
    public CommandLineRunner seedSampleData(
            UserRepository userRepository,
            MusicRecordRepository musicRecordRepository,
            PasswordEncoder passwordEncoder,
            ReviewRepository reviewRepository
    ){
        return args -> {
            if(userRepository.count()>1){
                System.out.println("Sample users already seeded.");
                return;
            }

            User user1 = new User (null, "sofia", "sofia@email.com", passwordEncoder.encode("password123"), Role.USER, new ArrayList<>(),new ArrayList<>());
            User user2 = new User (null, "thais", "thais@email.com", passwordEncoder.encode("password123"), Role.USER, new ArrayList<>(),new ArrayList<>());
            userRepository.saveAll(Arrays.asList(user1, user2));
            System.out.println("Sample users seeded");

            MusicRecord musicRecord1 = new MusicRecord (null, "When We All Fall Asleep, Where Do We Go?", "Billie Eilish", "Pop", 2019, "1. !!!! " + "2. bad guy " + "3. xanny " + "4. you should see me in a crown " + "5. all the good girls go to hell " +
                    "6. wish you were gay " + "7. when the party's over "+ "8. 8 "+ "9. my strange addiction " + "10. bury a friend " +
                    "11. ilomilo " + "12. listen before i go " + "13. i love you " +"14. goodbye ", "https://m.media-amazon.com/images/I/81idxQqxTlL.jpg", user1, new ArrayList<>());
            musicRecordRepository.saveAll(Arrays.asList(musicRecord1));
            System.out.println("Sample music RECORD seeded");

            Review review1 = new Review(null, 5, "Best album!", user1, musicRecord1);
            reviewRepository.saveAll(Arrays.asList(review1));
            System.out.println("Sample review seeded");

        };
    }
}
