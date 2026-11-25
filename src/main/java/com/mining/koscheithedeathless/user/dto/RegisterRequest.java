package com.mining.koscheithedeathless.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String familiya;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    private String otchestvo;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6)
    private String password;

    @NotNull(message = "Выберите роль")
    private Long roleId;

    private Long mineId;
}