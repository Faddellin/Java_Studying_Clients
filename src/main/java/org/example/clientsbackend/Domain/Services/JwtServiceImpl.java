package org.example.clientsbackend.Domain.Services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.example.clientsbackend.Application.ServicesInterfaces.JwtService;
import org.example.clientsbackend.Domain.Entities.User;
import org.example.clientsbackend.Domain.Enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access-token.secret}")
    private String jwtSigningKey;

    @Value("${jwt.access-token.lifetime-minutes}")
    private Long jwtLifetimeInMinutes;

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("userRole", user.getUserRole());

        return generateToken(claims, user.getUsername());
    }

    public String getUsernameFromToken(String token){
        return getTokenClaims(token).getSubject();
    }

    public UserRole getRoleFromToken(String token){
        return UserRole.valueOf(getTokenClaims(token).get("userRole", String.class));
    }

    public Claims getTokenClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            //log.error("Token expired", e);
        } catch (UnsupportedJwtException e) {
            //log.error("Unsupported jwt", e);
        } catch (MalformedJwtException e) {
            //log.error("Malformed jwt", e);
        } catch (SignatureException e) {
            //log.error("Invalid signature", e);
        } catch (Exception e) {
            //log.error("invalid token", e);
        }
        return false;
    }

    private String generateToken(Map<String, Object> extraClaims, String userSubject) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userSubject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(LocalDateTime.now()
                        .plusMinutes(jwtLifetimeInMinutes)
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
                )
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSigningKey));
    }
}
