package com.mining.koscheithedeathless.user.controller;

import com.mining.koscheithedeathless.user.dto.CreateUserRequest;
import com.mining.koscheithedeathless.user.model.User;
import com.mining.koscheithedeathless.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    // 1. Список всех пользователей
    @GetMapping
    public String listUsers(Model model) {
        // Используем DTO для отображения (без пароля)
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    // 2. Показать форму создания
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userForm", new CreateUserRequest());
        model.addAttribute("roles", User.Role.values()); // Передаем роли для выпадающего списка
        return "users/form";
    }

    // 3. Обработать отправку формы
    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userForm") CreateUserRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", User.Role.values()); // Возвращаем роли при ошибке
            return "users/form";
        }

        try {
            userService.createUser(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Пользователь \"" + request.getName() + "\" успешно создан!");
        } catch (IllegalArgumentException e) {
            // Обработка ошибки дубликата email
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            model.addAttribute("roles", User.Role.values());
            return "users/form";
        }

        return "redirect:/users";
    }
}