package hangoutsclone.hangouts.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

    SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

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
