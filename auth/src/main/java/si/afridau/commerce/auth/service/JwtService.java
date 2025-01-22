package si.afridau.commerce.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String AUTHORITIES_KEY = "auths";
    private final String securitySecretKey;
    private final long expirationTime;

    public JwtService(@Value("${security.constant.secret}") String securitySecretKey,
                      @Value("${security.constant.expirationTime}") long expirationTime) {
        this.securitySecretKey = securitySecretKey;
        this.expirationTime = expirationTime;

    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Collection<GrantedAuthority> extractAuthorities(String token) {
         return extractClaims(token, AUTHORITIES_KEY, ArrayList.class)
                 .stream()
                 .map(role -> new SimpleGrantedAuthority((String) role))
                 .toList();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(AUTHORITIES_KEY, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(@NotNull Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private <T> T extractClaims(String token, String key, Class<T> type) {
        final Claims claims = extractAllClaims(token);
        return claims.get(key, type);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(securitySecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
