package com.mining.koscheithedeathless.user;

import com.mining.koscheithedeathless.organization.Mine;
import com.mining.koscheithedeathless.organization.MineRepository;
import com.mining.koscheithedeathless.role.Role;
import com.mining.koscheithedeathless.role.RoleRepository;
import com.mining.koscheithedeathless.user.dto.CreateUserRequest;
import com.mining.koscheithedeathless.user.dto.RegisterRequest;
import com.mining.koscheithedeathless.user.dto.UpdateUserRequest;
import com.mining.koscheithedeathless.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
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

    // 1. Создание АДМИНОМ (сразу активен)
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // Валидация уникальности логина
        if (userRepository.existsByUsername(request.getUsername().trim())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setFamiliya(request.getFamiliya().trim());
        user.setName(request.getName().trim());
        // Безопасная установка отчества
        user.setOtchestvo(StringUtils.hasText(request.getOtchestvo()) ? request.getOtchestvo().trim() : null);
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true); // Админ создает сразу активного

        // Установка роли и рудника (вынесено в отдельный метод)
        assignRoleAndMine(user, request.getRoleId(), request.getMineId());

        return new UserResponse(userRepository.save(user));
    }

    // 2. Самостоятельная регистрация (требует модерации)
    @Transactional
    public void registerSelf(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername().trim())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setFamiliya(request.getFamiliya().trim());
        user.setName(request.getName().trim());
        // Безопасная установка отчества
        user.setOtchestvo(StringUtils.hasText(request.getOtchestvo()) ? request.getOtchestvo().trim() : null);
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ГЛАВНОЕ ОТЛИЧИЕ: Пользователь неактивен до подтверждения!
        user.setEnabled(false);

        // Установка роли и рудника
        assignRoleAndMine(user, request.getRoleId(), request.getMineId());

        userRepository.save(user);
    }

    // 3. Утверждение пользователя администратором
    @Transactional
    public void approveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    // 4. Обновление (АДМИНОМ)
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String newUsername = request.getUsername().trim();
        if (!user.getUsername().equals(newUsername)) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new IllegalArgumentException("Этот логин уже занят другим пользователем");
            }
            user.setUsername(newUsername);
        }

        user.setFamiliya(request.getFamiliya().trim());
        user.setName(request.getName().trim());
        // Безопасная установка отчества
        user.setOtchestvo(StringUtils.hasText(request.getOtchestvo()) ? request.getOtchestvo().trim() : null);
        user.setEmail(request.getEmail().trim());

        // Установка роли и рудника
        assignRoleAndMine(user, request.getRoleId(), request.getMineId());

        return new UserResponse(userRepository.save(user));
    }

    // 5. Блокировка (Soft Delete)
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    // =========================================================================
    // ВСПОМОГАТЕЛЬНЫЙ МЕТОД (DRY)
    // =========================================================================
    /**
     * Устанавливает роль и рудник пользователю с проверкой бизнес-логики.
     */
    private void assignRoleAndMine(User user, Long roleId, Long mineId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Роль не найдена"));

        // ИСПРАВЛЕНО: изменяемый HashSet вместо иммутабельного Set.of
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        if (mineId != null) {
            Mine mine = mineRepository.findById(mineId)
                    .orElseThrow(() -> new IllegalArgumentException("Рудник не найден"));
            user.setMine(mine);
        } else {
            if (!role.isHoldingRole()) {
                throw new IllegalArgumentException("Для должности '" + role.getTitle() + "' необходимо выбрать рудник");
            }
            user.setMine(null);
        }
    }
}