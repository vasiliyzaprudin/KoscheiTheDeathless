package com.mining.koscheithedeathless.user.service;

import com.mining.koscheithedeathless.user.dto.CreateUserRequest;
import com.mining.koscheithedeathless.user.dto.UserResponse;
import com.mining.koscheithedeathless.user.model.User;
import com.mining.koscheithedeathless.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok создаст конструктор для final полей
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = findUserEntityById(id);
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Хэшируем пароль!
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    // Вспомогательный метод (не отдается в контроллер)
    private User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + id));
    }
}