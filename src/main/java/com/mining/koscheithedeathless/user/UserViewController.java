package com.mining.koscheithedeathless.user;

import com.mining.koscheithedeathless.organization.MineRepository;
import com.mining.koscheithedeathless.role.RoleRepository;
import com.mining.koscheithedeathless.user.dto.CreateUserRequest;
import com.mining.koscheithedeathless.user.dto.UpdateUserRequest;
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
    private final MineRepository mineRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

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
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("mines", mineRepository.findAll()); // <-- Передаем рудники
        return "users/form";
    }
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();

        // Мапим User в UpdateUserRequest
        UpdateUserRequest req = new UpdateUserRequest();
        req.setUsername(user.getUsername());
        req.setFamiliya(user.getFamiliya());
        req.setName(user.getName());
        req.setOtchestvo(user.getOtchestvo());
        if (!user.getRoles().isEmpty()) req.setRoleId(user.getRoles().iterator().next().getId());
        if (user.getMine() != null) req.setMineId(user.getMine().getId());

        model.addAttribute("userForm", req);
        model.addAttribute("userId", id);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("mines", mineRepository.findAll());
        return "users/edit"; // Нужно будет создать edit.html
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
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("mines", mineRepository.findAll());
            return "users/form";
        }

        try {
            userService.createUser(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Пользователь \"" + request.getName() + "\" успешно создан!");
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("mines", mineRepository.findAll());
            return "users/form";
        }

        return "redirect:/users";
    }
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("userForm") UpdateUserRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("mines", mineRepository.findAll());
            return "users/edit";
        }
        userService.updateUser(id, request);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
