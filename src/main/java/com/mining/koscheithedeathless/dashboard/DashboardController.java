package com.mining.koscheithedeathless.dashboard;

import com.mining.koscheithedeathless.security.CurrentUserService;
import com.mining.koscheithedeathless.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor // <-- Lombok создаст конструктор для CurrentUserService
public class DashboardController {

    private final CurrentUserService currentUserService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Вот вся магия! Одна строчка — и у нас готовый пользователь.
        User currentUser = currentUserService.getCurrentUser();

        model.addAttribute("user", currentUser);

        if (currentUser.getMine() != null) {
            model.addAttribute("mine", currentUser.getMine());
            model.addAttribute("isHolding", false);
        } else {
            model.addAttribute("isHolding", true);
        }

        return "dashboard";
    }
}
