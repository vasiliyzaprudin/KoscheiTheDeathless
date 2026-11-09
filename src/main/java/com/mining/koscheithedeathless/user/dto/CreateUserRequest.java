package com.mining.koscheithedeathless.user.dto;

import com.mining.koscheithedeathless.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // Lombok: геттеры, сеттеры, toString
public class CreateUserRequest {

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    @NotNull(message = "Выберите роль")
    private User.Role role;
}