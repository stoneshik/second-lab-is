package lab.is.security.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lab.is.exceptions.UserNotFoundException;
import lab.is.security.bd.entities.User;
import lab.is.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User loadUserById(Long userId) {
        return userRepository
            .findById(userId)
            .orElseThrow(
                () -> new UserNotFoundException("Id пользователя не найден: " + userId)
            );
    }
}
