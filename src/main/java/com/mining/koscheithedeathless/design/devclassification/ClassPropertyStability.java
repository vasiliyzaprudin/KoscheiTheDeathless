package com.mining.koscheithedeathless.design.devclassification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "class_property_stability")
public class ClassPropertyStability {

    // Составной первичный ключ (или просто ID, как удобнее)
    // Сделаем просто автоинкрементный ID, так надежнее для JPA
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