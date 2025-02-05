package hangoutsclone.hangouts.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyStr;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (secretKeyStr != null && !secretKeyStr.trim().isEmpty()) {
            this.secretKey = Keys.hmacShaKeyFor(secretKeyStr.getBytes());
        } else {
            this.secretKey = Jwts.SIG.HS256.key().build();
            }
        }

    public String generateToken(Authentication authentication) {
        String jwt = Jwts.builder()
                .claim("iss", "User")
                .claim("iat", new Date())
                .claim("exp", new Date(System.currentTimeMillis() + 864000000))
                .claim("email", authentication.getName())
                .signWith(secretKey)
                .compact();
        return jwt;
    }

    public String getEmailFromToken(String token) {
        token = token.substring(7);

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String email = String.valueOf(claims.get("email"));

        return email;
    }
}
