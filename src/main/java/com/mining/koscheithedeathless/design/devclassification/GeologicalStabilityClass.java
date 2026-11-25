package com.mining.koscheithedeathless.design.devclassification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "geological_stability_classes")
public class GeologicalStabilityClass {
    // Справочник геологических классов устойчивости горного массива
    //(geological_stability_class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long geologicalStabilityClassId;

    @Column(nullable = false, unique = true, length = 10)
    private String geologicalStabilityClassCode;

}