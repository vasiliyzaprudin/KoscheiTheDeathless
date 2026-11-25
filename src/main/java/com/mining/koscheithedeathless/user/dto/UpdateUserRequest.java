package com.mining.koscheithedeathless.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String familiya;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    private String otchestvo;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotNull(message = "Выберите роль")
    private Long roleId;

    private Long mineId;

    // ВАЖНО: Пустой метод setUsername() удален, Lombok сгенерирует правильный.
}