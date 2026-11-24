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
    private Long mine_id;

    @Column(nullable = false, unique = true, length = 50)
    private String mine_code;

    @Column(nullable = false)
    private String mine_name;

    @Column(nullable = false)
    private boolean mine_enabled = true;

    @Column(length = 500)
    private String mine_address;

    @Column(length = 1000)
    private String mine_description;
}
