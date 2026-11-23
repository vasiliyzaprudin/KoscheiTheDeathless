package com.mining.koscheithedeathless.config;

import com.mining.koscheithedeathless.role.Role;
import com.mining.koscheithedeathless.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    createRole("ADMIN", "Администратор"),
                    createRole("TUNNELER", "Проходчик"),
                    createRole("SURVEYOR", "Маркшейдер"),
                    createRole("GEOLOGIST", "Геолог"),
                    createRole("MINE_MANAGER", "Главный инженер"),
                    createRole("ENGINEER", "Пневмостулонадуватель"),
                    createRole("VIEWER", "Наблюдатель"),
                    createRole("HOLDING_ANALYST", "Главный геомеханик компании")
            ));
            System.out.println("✅ Базовые роли успешно добавлены в БД!");
        }
    }

    private Role createRole(String code, String title) {
        Role r = new Role();
        r.setCode(code);
        r.setTitle(title);
        return r;
    }
}