package com.mining.koscheithedeathless.design.devclassification;

import com.mining.koscheithedeathless.organization.Mine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "geological_stability_class_mines")
public class GeologicalStabilityClassMine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long geologicalStabilityClassMineId;

    // ID рудника (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mine_id", nullable = false)
    private Mine mine;

    // ID глобального класса из справочника (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geological_stability_class_id", nullable = false)
    private GeologicalStabilityClass geologicalStabilityClass;

    // Код, который проектировщик задал для этого рудника (например, "1", "2", "3")
    @Column(nullable = false, length = 50)
    private String geologicalStabilityClassMineCode;
}