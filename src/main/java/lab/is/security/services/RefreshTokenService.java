package lab.is.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.exceptions.TokenRefreshException;
import lab.is.security.bd.entities.RefreshToken;
import lab.is.security.bd.entities.User;
import lab.is.security.jwt.TokenProperties;
import lab.is.security.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final TokenProperties tokenProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userService.loadUserById(userId);
        RefreshToken refreshToken = RefreshToken.builder()
            .user(user)
            .expiryDate(Instant.now().plusMillis(tokenProperties.getExpireTime()))
            .token(UUID.randomUUID().toString())
            .build();
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(
                "Срок действия токена обновления истек. Пожалуйста, сделайте новый запрос на вход"
            );
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        User user = userService.loadUserById(userId);
        return refreshTokenRepository.deleteByUser(user);
    }
}
