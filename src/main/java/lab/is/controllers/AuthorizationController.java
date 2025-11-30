package lab.is.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lab.is.dto.responses.MessageResponseDto;
import lab.is.exceptions.ResourceIsAlreadyExistsException;
import lab.is.exceptions.RoleNotFoundException;
import lab.is.exceptions.TokenRefreshException;
import lab.is.security.bd.entities.RefreshToken;
import lab.is.security.bd.entities.Role;
import lab.is.security.bd.entities.RoleEnum;
import lab.is.security.bd.entities.User;
import lab.is.security.dto.request.LoginRequestDto;
import lab.is.security.dto.request.LogoutRequestDto;
import lab.is.security.dto.request.RegisterRequestDto;
import lab.is.security.dto.request.TokenRefreshRequestDto;
import lab.is.security.dto.response.JwtResponseDto;
import lab.is.security.dto.response.TokenRefreshResponseDto;
import lab.is.security.jwt.JwtUtils;
import lab.is.security.model.UserDetailsImpl;
import lab.is.security.repositories.RoleRepository;
import lab.is.security.repositories.UserRepository;
import lab.is.security.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDto.getLogin(),
                loginRequestDto.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(
            JwtResponseDto.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService
            .findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
                String token = jwtUtils.generateTokenFromUsername(user.getLogin());
                return ResponseEntity.ok(
                    TokenRefreshResponseDto.builder()
                        .accessToken(token)
                        .refreshToken(requestRefreshToken)
                        .build()
                );
            })
            .orElseThrow(() ->
                new TokenRefreshException("Токен обновления не в базе данных!")
            );
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> register(@Valid @RequestBody RegisterRequestDto signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByLogin(signUpRequest.getLogin()))) {
            throw new ResourceIsAlreadyExistsException("Логин уже занят");
        }
        User user = User.builder()
            .login(signUpRequest.getLogin())
            .password(encoder.encode(signUpRequest.getPassword()))
            .build();
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository
                .findByName(RoleEnum.ROLE_USER)
                .orElseThrow(RoleNotFoundException::new);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository
                        .findByName(RoleEnum.ROLE_ADMIN)
                        .orElseThrow(RoleNotFoundException::new);
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository
                        .findByName(RoleEnum.ROLE_USER)
                        .orElseThrow(RoleNotFoundException::new);
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponseDto("Пользователь успешно зарегистрирован!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDto> logoutUser(@Valid @RequestBody LogoutRequestDto logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponseDto("Пользователь успешно вышел!"));
    }
}
