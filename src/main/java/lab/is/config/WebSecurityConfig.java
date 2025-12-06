package lab.is.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lab.is.security.jwt.AuthEntryPointJwt;
import lab.is.security.jwt.AuthTokenFilter;
import lab.is.security.jwt.JwtUtils;
import lab.is.security.repositories.RefreshTokenRepository;
import lab.is.security.services.UserDetailsServiceImpl;
import lab.is.security.services.UserService;
import lombok.RequiredArgsConstructor;

@Profile({"dev", "helios", "default"})
@Configuration
@ComponentScan
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService, userService, refreshTokenRepository);
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(
                exception-> exception.authenticationEntryPoint(unauthorizedHandler)
            )
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/logout",
                    "/music-bands",
                    "/music-bands/**",
                    "/coordinates",
                    "/coordinates/**",
                    "/albums",
                    "/albums/**",
                    "/studios",
                    "/studios/**",
                    "/nominations",
                    "/nominations/**",
                    "/index.html",
                    "/favicon.ico",
                    "/assets/**",
                    "/auth/login",
                    "/auth/refresh-token",
                    "/auth/register"
                ).permitAll()
                .anyRequest().authenticated()
            );
        http.addFilterBefore(
            authenticationJwtTokenFilter(),
            UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
}
