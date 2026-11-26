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
    private Long mineId;

    @Column(nullable = false, unique = true, length = 50)
    private String mineCode;

    @Column(nullable = false)
    private String mineName;

    @Column(nullable = false)
    private boolean mineEnabled = true;

    @Column(length = 500)
    private String mineAddress;

    @Column(length = 1000)
    private String mineDescription;
}
