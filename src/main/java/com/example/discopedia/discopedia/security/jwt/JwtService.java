package com.example.discopedia.discopedia.security.jwt;

import com.example.discopedia.discopedia.security.CustomUserDetail;
import com.example.discopedia.discopedia.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.security.cert.X509CertSelector;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String JWT_SECRET_KEY="mySecretKeyForJWTTokenGenerationThatIsAtLeast256BitsLong";
    private final Long JWT_EXPIRATION=1800000L;
    private final String ROLE = "role";
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    
    public String generateToken(CustomUserDetail userDetail){return buildToken(userDetail, JWT_EXPIRATION);}

    private String buildToken(CustomUserDetail userDetail, Long jwtExpiration) {
        return Jwts
                .builder()
                .claim(ROLE, userDetail.getAuthorities().toString())
                .subject(userDetail.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ jwtExpiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername (String token){return extractAllClaims(token).getSubject();}

    public boolean isValidToken(String token){
        try{
            extractAllClaims(token);
            return true;
        } catch (Exception exception){
            return false;
        }
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Object getSignKey() {
    }
}
