package lab.is.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lab.is.security.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final TokenProperties tokenProperties;

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + tokenProperties.getExpireTime()))
            .signWith(SignatureAlgorithm.HS256, tokenProperties.getSecretKey())
            .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts
            .parser()
            .setSigningKey(tokenProperties.getSecretKey())
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(tokenProperties.getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Неправильный JWT токен: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Срок действия токена JWT истек: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT токен не поддерживается: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT строка требований пуста: {}", e.getMessage());
        }
        return false;
    }
}
