package com.mining.koscheithedeathless.user;

import com.mining.koscheithedeathless.organization.Mine;
import com.mining.koscheithedeathless.organization.MineRepository;
import com.mining.koscheithedeathless.role.Role;
import com.mining.koscheithedeathless.role.RoleRepository;
import com.mining.koscheithedeathless.user.dto.CreateUserRequest;
import com.mining.koscheithedeathless.user.dto.UpdateUserRequest;
import com.mining.koscheithedeathless.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MineRepository mineRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + id));
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setFamiliya(request.getFamiliya().trim());
        user.setName(request.getName().trim());
        user.setOtchestvo(request.getOtchestvo().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Ищем роль в базе по ID
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Роль не найдена"));

        user.setRoles(Set.of(role));

        // Логика рудника
        if (request.getMineId() != null) {
            Mine mine = mineRepository.findById(request.getMineId())
                    .orElseThrow(() -> new IllegalArgumentException("Рудник не найден"));
            user.setMine(mine);
        } else {
            // Если рудник не выбран, проверяем, разрешено ли это для роли
            if (!role.isHoldingRole()) {
                throw new IllegalArgumentException("Для должности '" + role.getTitle() + "' необходимо выбрать рудник");
            }
            user.setMine(null);
        }

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!user.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Этот логин уже занят другим пользователем");
            }
            user.setUsername(request.getUsername().trim());
        }

        user.setFamiliya(request.getFamiliya());
        user.setName(request.getName());
        user.setOtchestvo(request.getOtchestvo());
        user.setEmail(request.getEmail());

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow();
        user.setRoles(Set.of(role));

        if (request.getMineId() != null) {
            Mine mine = mineRepository.findById(request.getMineId()).orElseThrow();
            user.setMine(mine);
        } else {
            if (!role.isHoldingRole()) throw new IllegalArgumentException("Выберите рудник");
            user.setMine(null);
        }
        return new UserResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(false); // Soft Delete: просто блокируем!
        userRepository.save(user);
    }
}