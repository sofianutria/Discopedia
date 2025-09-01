package com.example.discopedia.discopedia.security.jwt;

import com.example.discopedia.discopedia.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String JWT_SECRET_KEY="mySecretKeyForJWTTokenGenerationThatIsAtLeast256BitsLong";
    private final Long JWT_EXPIRATION=1800000L;
    private final String ROLE = "role";
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
}
