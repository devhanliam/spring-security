package com.example.springsecurity.service;

import com.example.springsecurity.authentication.JwtToken;
import com.example.springsecurity.exception.BadJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
@PropertySource("classpath:jwt.yml")
public class JwtService {
    private final String secretKey;
    private final long expirationHours;
    private final String issuer;
    private final Key key;
    public final static String AUTH_TYPE = "Bearer";
    public final static String CLAIM_NAME = "auth";


    public JwtService(@Value("${secret-key}") String secretKey,
                      @Value("${expiration-hours}") long expirationHours,
                      @Value("${issuer}") String issuer) {
        this.secretKey = secretKey;
        this.expirationHours = expirationHours;
        this.issuer = issuer;
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }



    public JwtToken generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(CLAIM_NAME, authorities)
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 36))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType(AUTH_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public Collection<? extends GrantedAuthority> getRoles(String token) {
        Claims claims = parseClaims(token);
        if (claims.get(CLAIM_NAME) == null) {
            throw new BadJwtException("권한 정보가 없는 토큰입니다.");
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(CLAIM_NAME).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return authorities;
    }

    public boolean validateToken(String token) {
        if(token == null){
            throw new BadJwtException("빈 값 토큰");
        }
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            claimsJws.getBody().getId();
            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new BadJwtException("잘못된 형식의 토큰");
        } catch (UnsupportedJwtException e) {
            throw new BadJwtException("지원하지않는 형식의 토큰");
        } catch (IllegalArgumentException e) {
            throw new BadJwtException("올바르지 않은 형식의 토큰");
        } catch (ExpiredJwtException e) {
           //토큰 재발급 지행
            return true;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            return e.getClaims();
        }
    }

}
