package com.mining.koscheithedeathless.auth;

import com.mining.koscheithedeathless.auth.dto.AuthRequest;
import com.mining.koscheithedeathless.auth.dto.AuthResponse;
import com.mining.koscheithedeathless.security.JwtService;
import com.mining.koscheithedeathless.user.User;
import com.mining.koscheithedeathless.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        // Аутентифицируем по логину
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Берем первую роль (для токена) - у нас она есть в authorities
        String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        // Находим юзера в базе по ЛОГИНУ (было findByEmail)
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Long mineId = user.getMine() != null ? user.getMine().getMine_id() : null;

        // Генерируем токен на основе ЛОГИНА
        String token = jwtService.generateToken(userDetails.getUsername(), role, mineId);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}