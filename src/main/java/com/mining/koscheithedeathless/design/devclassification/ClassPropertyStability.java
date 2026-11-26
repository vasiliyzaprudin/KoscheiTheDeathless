package com.mining.koscheithedeathless.design.devclassification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "class_property_stability",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_class_property",
                columnNames = {"geological_stability_class_id", "property_stability_id"}))
public class ClassPropertyStability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classPropertyStabilityId;

    // Ссылка на класс устойчивости
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geological_stability_class_id", nullable = false)
    private GeologicalStabilityClass geologicalStabilityClass;

    // Ссылка на свойство
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_stability_id", nullable = false)
    private PropertyStability propertyStability;

    // Обязательно ли это свойство для данного класса
    @Column(nullable = false)
    private boolean isRequired;
}