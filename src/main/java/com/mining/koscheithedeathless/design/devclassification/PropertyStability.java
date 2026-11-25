package com.mining.koscheithedeathless.design.devclassification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "property_stabilities")
public class PropertyStability {
    // Справочник свойств устойчивости горного массива (property_stability)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyStabilityId;

    @Column(nullable = false, unique = true)
    private String propertyStabilityName;
    private String unit; // МПа, м, градусы, -
    private String dataType; // число, текст

    @Column
    private String propertyStabilityNameRu;
}