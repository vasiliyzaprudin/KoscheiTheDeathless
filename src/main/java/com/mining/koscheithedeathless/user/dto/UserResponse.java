package com.mining.koscheithedeathless.user.dto;

import com.mining.koscheithedeathless.user.model.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final User.Role role;
    private final boolean enabled;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.enabled = user.isEnabled();
    }
}