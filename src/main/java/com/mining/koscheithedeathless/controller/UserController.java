package com.mining.koscheithedeathless.controller;

import com.mining.koscheithedeathless.dto.CreateUserDto;
import com.mining.koscheithedeathless.model.User;
import com.mining.koscheithedeathless.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. Список всех пользователей
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list";       // ← шаблон templates/users/list.html
    }

    // 2. Показать форму создания
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userForm", new CreateUserDto()); // ← пустой DTO для формы
        return "users/form";       // ← шаблон templates/users/form.html
    }

    // 3. Обработать отправку формы
    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userForm") CreateUserDto dto,
            BindingResult bindingResult,          // ← сюда попадут ошибки валидации
            RedirectAttributes redirectAttributes
    ) {
        // Если есть ошибки валидации — вернуть форму с ошибками
        if (bindingResult.hasErrors()) {
            return "users/form";
        }
        // Сохранить в БД
        User saved = userService.save(dto);

        // Сообщение для следующей страницы (переживает редирект)
        redirectAttributes.addFlashAttribute("successMessage",
                "Пользователь \"" + saved.getName() + "\" успешно создан!");

        // Редирект на список (PRG-паттерн: Post/Redirect/Get)
        return "redirect:/users";
    }

    // 4. Просмотр одного пользователя
    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("title", "Профиль пользователя");
        return "userPage";
    }
}
