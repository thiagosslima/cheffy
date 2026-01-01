package br.com.fiap.cheffy.service.security;

import br.com.fiap.cheffy.config.properties.JwtProperties;
import br.com.fiap.cheffy.exceptions.TokenExpiredException;
import br.com.fiap.cheffy.model.security.AuthenticatedUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import static br.com.fiap.cheffy.model.enums.ExceptionsKeys.TOKEN_EXPIRED_EXCEPTION;

@Service
public class JwtService {

    private final Key key;
    private final long expiration;
    private final String issuer;

    public JwtService(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(
                props.getSecret().getBytes(StandardCharsets.UTF_8)
        );
        this.expiration = props.getExpiration();
        this.issuer = props.getIssuer();
    }

       public String generateToken(AuthenticatedUser user) {
        return Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getUsername())
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("type", "access_token")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(parseClaims(token).getId());
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);

            return "access_token".equals(claims.get("type"));

        } catch (TokenExpiredException e){
            throw e;
        } catch (Exception ex) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
           return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(issuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(TOKEN_EXPIRED_EXCEPTION);
        }
    }
}
