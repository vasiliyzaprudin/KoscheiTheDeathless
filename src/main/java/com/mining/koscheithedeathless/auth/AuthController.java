package com.mining.koscheithedeathless.auth;

import com.mining.koscheithedeathless.auth.dto.AuthRequest;
import com.mining.koscheithedeathless.auth.dto.AuthResponse;
import com.mining.koscheithedeathless.security.JwtService;
import com.mining.koscheithedeathless.user.User;
import com.mining.koscheithedeathless.user.UserRepository;
import com.mining.koscheithedeathless.user.UserService;
import com.mining.koscheithedeathless.user.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            Long mineId = user.getMine() != null ? user.getMine().getMineId() : null;

            String token = jwtService.generateToken(userDetails.getUsername(), role, mineId);
            return ResponseEntity.ok(new AuthResponse(token, role));

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Аккаунт ожидает подтверждения администратора."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Неверный логин или пароль."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("message", errorMsg));
        }
        try {
            userService.registerSelf(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Заявка отправлена. Ожидайте подтверждения администратора."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}