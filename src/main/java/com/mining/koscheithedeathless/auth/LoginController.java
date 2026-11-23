package com.mining.koscheithedeathless.auth;


import com.mining.koscheithedeathless.organization.MineRepository;
import com.mining.koscheithedeathless.role.RoleRepository;
import com.mining.koscheithedeathless.user.User;
import com.mining.koscheithedeathless.user.UserService;
import com.mining.koscheithedeathless.user.dto.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final MineRepository mineRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // --- РЕГИСТРАЦИЯ ---

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new CreateUserRequest());

        // Берем роли из базы, исключая ADMIN
        model.addAttribute("roles", roleRepository.findAll().stream()
                .filter(r -> !r.getCode().equals("ADMIN"))
                .toList());

        model.addAttribute("mines", mineRepository.findAll());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userForm") CreateUserRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        // Если есть ошибки валидации (например, пустое имя или короткий пароль)
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll().stream()
                    .filter(r -> !r.getCode().equals("ADMIN"))
                    .toList());
            model.addAttribute("mines", mineRepository.findAll());
            return "register";
        }

        try {
            userService.createUser(request);
            // После успешной регистрации отправляем на страницу входа
            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            // Если email занят или не выбран рудник для обычной роли
            bindingResult.rejectValue("email", "error.email", e.getMessage());

            model.addAttribute("roles", roleRepository.findAll().stream()
                    .filter(r -> !r.getCode().equals("ADMIN"))
                    .toList());
            model.addAttribute("mines", mineRepository.findAll());
            return "register";
        }
    }
}