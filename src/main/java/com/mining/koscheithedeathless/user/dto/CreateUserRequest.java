package com.mining.koscheithedeathless.user.dto;

import com.mining.koscheithedeathless.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 100)
    private String familiya;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Вы живете в стране где уважают Отца")
    @Size(min = 2, max = 100)
    private String otchestvo;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    @NotNull(message = "Выберите роль")
    private Long roleId;

    private Long mineId;
}