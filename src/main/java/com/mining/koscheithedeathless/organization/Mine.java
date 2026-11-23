package com.mining.koscheithedeathless.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mines")
public class Mine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Короткий код для отчетов, например "MINE-SIB-01"
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // Полное название
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(length = 500)
    private String address; // Юридический или фактический адрес

    @Column(length = 1000)
    private String description; // Краткое описание (тип добычи, особенности)

    public Mine(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
