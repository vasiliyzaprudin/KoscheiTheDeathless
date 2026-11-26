package com.mining.koscheithedeathless.design.devclassification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "range_value_property_stability_name_mines")
public class RangeValuePropertyStabilityNameMine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long valuePropertyStabilityMineId;

    // ID класса рудника (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geological_stability_class_mine_id", nullable = false)
    private GeologicalStabilityClassMine geologicalStabilityClassMine;

    // ID свойства из глобального справочника (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_stability_id", nullable = false)
    private PropertyStability propertyStability;

    // ID выбранного диапазона значений из глобального справочника (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "value_property_stability_id")
    private RangeValuePropertyStabilityName valuePropertyStability;
}