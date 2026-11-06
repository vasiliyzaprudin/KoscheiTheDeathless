package com.mining.koscheithedeathless.service;

import com.mining.koscheithedeathless.dto.CreateUserDto;
import com.mining.koscheithedeathless.model.User;
import com.mining.koscheithedeathless.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public User save(CreateUserDto dto) {
        User user = new User();
        user.setName(dto.getName().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        return userRepository.save(user);
    }

    public java.util.List<User> findAll() {
        return userRepository.findAll();
    }
}