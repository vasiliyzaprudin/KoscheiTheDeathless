package com.mining.koscheithedeathless.security;

import com.mining.koscheithedeathless.user.User;
import com.mining.koscheithedeathless.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Если пользователь не авторизован (или это анонимный вход на страницу логина)
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Пользователь не авторизован");
        }

        String username = authentication.getName();

        // Ищем в базе по логину
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь с логином " + username + " не найден"));
    }
}
/*
***Этот класс выше создан для того, чтобы не писать один и тот же код поиска пользователя в каждом контроллере
***и пользоваться вот этим выражением
*
*
*
 @GetMapping("/excavation/documents")
public String listDocuments(Model model) {
    User user = currentUserService.getCurrentUser();

    if (user.getMine() != null) {
        // Проходчик или Геолог. Показываем только его рудник.
        model.addAttribute("documents", documentService.findByMine(user.getMine()));
    } else {
        // Сотрудник УК. Показываем всё (или даем выбрать рудник).
        model.addAttribute("documents", documentService.findAll());
    }
    return "excavation/list";
}
*
*
***Что дает:
    Чистая доменная модель: Класс User ничего не знает про Spring Security.

    Никаких дубликатов: Логика получения текущего юзера написана в одном месте.

     Транзакционность: Мы пометили getCurrentUser как @Transactional(readOnly = true).
Это значит, что если внутри контроллера мы обратимся к currentUser.getMine().getName(),
 Hibernate не выдаст ошибку LazyInitializationException, так как сессия еще открыта.

     Безопасность: Если кто-то вызовет метод без авторизации,
 мы явно выкинем исключение, а не словим NullPointerException.
 */