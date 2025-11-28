package rafikibora.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import rafikibora.handlers.LogUtil;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtProvider implements JwtProviderI {
    @Value("${rafiki-bora.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${rafiki-bora.auth.tokenExpirationMsec}")
    private Long tokenExpirationMsec;

    @Value("${rafiki-bora.auth.refreshTokenExpirationMsec}")
    private Long refreshTokenExpirationMsec;

    @Override
    public String generateToken(UserDetails user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getAuthorities().stream().map(s ->
                new SimpleGrantedAuthority(s.getAuthority())).
                filter(Objects::nonNull).
                collect(Collectors.toList()));
        Date now = new Date();
        Long duration = now.getTime() + tokenExpirationMsec;
        Date expiryDate = new Date(duration);
        Key key = getSigningKey();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiryDate)
                .compact();

    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getAuthorities().stream().map(s ->
                new SimpleGrantedAuthority(s.getAuthority())).
                filter(Objects::nonNull).
                collect(Collectors.toList()));
        Date now = new Date();
        Long duration = now.getTime() + refreshTokenExpirationMsec;
        Date expiryDate = new Date(duration);
        Key key = getSigningKey();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiryDate)
                .compact();

    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    @Override
    public LocalDateTime getExpiryDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            LogUtil.logException(log, "Invalid JWT encountered during validation", ex);
        }
        return false;
    }

    /**
     * Builds a secure signing key for HS512 using best practices.
     *
     * Behavior:
     * - If tokenSecret is Base64-encoded, use the decoded bytes.
     * - If not Base64, fall back to raw UTF-8 bytes.
     * - If resulting bytes are shorter than 64 bytes (HS512 requirement),
     *   derive a 64-byte key by hashing with SHA-512 (deterministic) and warn.
     */
    private Key getSigningKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(tokenSecret);
        } catch (IllegalArgumentException e) {
            // Not Base64; fallback to raw bytes
            keyBytes = tokenSecret.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < 64) {
            // Derive a 64-byte key using SHA-512 of the provided secret
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(keyBytes);
                byte[] derived = md.digest();
                log.warn("JWT secret is shorter than recommended for HS512. Deriving a strong key via SHA-512. Consider configuring a Base64-encoded 512-bit secret.");
                keyBytes = derived; // 64 bytes
            } catch (NoSuchAlgorithmException e) {
                // Fallback: jjwt will throw if size is inadequate; log and continue
                LogUtil.logException(log, "SHA-512 algorithm not available for key derivation", e);
            }
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
