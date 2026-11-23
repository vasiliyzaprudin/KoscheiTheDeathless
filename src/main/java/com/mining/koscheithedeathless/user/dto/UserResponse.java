package com.mining.koscheithedeathless.user.dto;

import com.mining.koscheithedeathless.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserResponse {
    private final Long id;
    private final String username;
    private final String name;
    private final String familiya;
    private final String otchestvo;
    private final String email;
    private final String mineName;
    private Set<String> roleTitles;
    private final boolean enabled;


    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.familiya = user.getFamiliya();
        this.otchestvo = user.getOtchestvo();
        this.email = user.getEmail();
        this.mineName = user.getMine() != null ? user.getMine().getName() : "Управляющая компания";
        this.enabled = user.isEnabled();
        this.roleTitles = user.getRoles().stream()
                .map(role -> role.getTitle())
                .collect(Collectors.toSet());
    }
}