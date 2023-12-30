package vn.healthcare.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {
    private static final String SECRET = "secret";

    private static final long TIME_EXPIRATION = 6000 * 60 * 1000;//thời gian hết hạn token:100h

    public String generateToken(Integer id, String role) {//sau khi login thành công-> gen token để trả về
        Date issuedAt = new Date();
        Date expirationAt = new Date(issuedAt.getTime() + TIME_EXPIRATION);

        return Jwts.builder().claim("role", role)
                .setSubject(id.toString()).setIssuedAt(issuedAt).setExpiration(expirationAt)
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public Integer getIdFromToken(String token) {
        return Integer.valueOf(Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getSubject());
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().get("role").toString();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}