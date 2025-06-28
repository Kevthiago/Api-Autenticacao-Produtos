package com.prova.av2.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            // Use Logger aqui se quiser
            System.err.println("Erro na validação do token: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        if (!validateToken(token)) {
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
        return JWT.decode(token).getSubject();
    }

    public Map<String, Object> getAllClaimsFromToken(String token) {
        var claims = JWT.decode(token).getClaims();
        Map<String, Object> map = new HashMap<>();
        claims.forEach((key, claim) -> {
            if (claim.asString() != null) {
                map.put(key, claim.asString());
            } else if (claim.asBoolean() != null) {
                map.put(key, claim.asBoolean());
            } else if (claim.asDate() != null) {
                map.put(key, claim.asDate());
            } else if (claim.asInt() != null) {
                map.put(key, claim.asInt());
            } else {
                map.put(key, claim.toString());
            }
        });
        return map;
    }
}
