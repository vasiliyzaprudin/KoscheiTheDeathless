package com.mining.koscheithedeathless.user;

import com.mining.koscheithedeathless.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mining.koscheithedeathless.organization.Mine;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String familiya;

    @Column(nullable = false, length = 100)
    private String otchestvo;

    @Column(nullable = false, length = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Привязка к руднику.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mineId")
    private Mine mine;

    @Column(nullable = false)
    private boolean enabled;

    public User() {}


    public User(String name, String familiya, String otchestvo, String email, String password, Set<Role> roles, Mine mine) {
        this.name = name;
        this.familiya = familiya;
        this.otchestvo = otchestvo;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.mine = mine;
        this.enabled = true;
    }
}