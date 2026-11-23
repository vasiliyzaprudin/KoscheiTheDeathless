package com.mining.koscheithedeathless.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Технический код для Java (например, "ADMIN")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // Русское название для интерфейса (например, "Администратор")
    @Column(nullable = false, length = 100)
    private String title;

    // МЕТОД ПРОВЕРКИ: Может ли эта роль быть без рудника?
    public boolean isHoldingRole() {
        return "ADMIN".equals(this.code) ||
                "HOLDING_ANALYST".equals(this.code) ||
                "VIEWER".equals(this.code);
    }
}