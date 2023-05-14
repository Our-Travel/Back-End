package com.example.ot.config.security.jwt;

import com.example.ot.util.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final SecretKey jwtSecretKey;

    private SecretKey getSecretKey() {
        return jwtSecretKey;
    }

    // 토큰 생성
    public String generateAccessToken(Map<String, Object> claims, long seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰 검증
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
