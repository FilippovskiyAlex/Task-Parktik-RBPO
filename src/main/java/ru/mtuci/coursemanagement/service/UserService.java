package ru.mtuci.coursemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mtuci.coursemanagement.dto.UserDto;
import ru.mtuci.coursemanagement.model.User;
import ru.mtuci.coursemanagement.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Optional<User> findByUsername(String u) {
        return repo.findByUsername(u);
    }

    public User save(UserDto u) {
        if (repo.findByUsername(u.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with this name already exists");
        }
        User e = new User();
        e.setUsername(u.getUsername());
        e.setPassword(passwordEncoder.encode(u.getPassword()));
        e.setRole("STUDENT");
        return repo.save(e);
    }
}