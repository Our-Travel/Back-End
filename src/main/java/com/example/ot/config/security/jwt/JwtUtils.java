package com.example.ot.config.security.jwt;

import com.example.ot.util.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final SecretKey jwtSecretKey;

    private SecretKey getSecretKey() {
        return jwtSecretKey;
    }

    public String generateAccessToken(Map<String, Object> claims) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * 60L * 60 * 24 * 365 * 100);

        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public String getMemberIdFromToken(String token){
        Map<String, Object> claims = getClaims(token);
        return String.valueOf(claims.get("id"));
    }

    public String extractJwt(final StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("Authorization");
    }

    public Map<String, Object> getClaims(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);

        return Util.json.toMap(body);
    }
}
