package lab.is.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lab.is.security.bd.entities.User;
import lab.is.security.model.UserDetailsImpl;
import lab.is.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
            .findByLogin(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Логин пользователя не найден: " + username)
            );
        return UserDetailsImpl.build(user);
    }
}
