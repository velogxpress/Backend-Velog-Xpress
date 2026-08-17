package com.velogexpress.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String SECRET = System.getenv().getOrDefault(
            "JWT_SECRET", "dev-only-insecure-default-change-me");

    // 🔐 Kreye key la yon sèl fwa
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ✅ GENERATE TOKEN AK ROLE
    public String generateToken(UserDetails userDetails) {

        // pran role user la nan Spring Security
        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_CLIENT");

        return Jwts.builder()
                .setSubject(userDetails.getUsername())

                // 👇 AJOUTE ROLE NAN JWT
                .claim("role", role.replace("ROLE_", "")) // ADMIN / AGENT / CLIENT

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ LI USERNAME
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ✅ LI ROLE (sa ap itil si backend bezwen li pita)
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 🔍 Parse JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ VALIDATION TOKEN
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
