package com.mining.koscheithedeathless.design.devclassification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "range_value_property_stability_name",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_range_value",
                        columnNames = {"property_stability_id", "range_value_property_stability_name"}
                )
        }
)
public class RangeValuePropertyStabilityName {
    // Справочник значений свойств устойчивости горного массива
    //(range_value_property_stability_name)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long valuePropertyStabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_stability_id", nullable = false)
    private PropertyStability propertyStability; // DS настаивает не писать в конце Id, т к это не число, а целый объект

    @Column(nullable = false)
    private String rangeValuePropertyStabilityName;


    private Double minValuePropertyStability;
    private Double maxValuePropertyStability;

    @Column
    private String rangeValuePropertyStabilityNameRu;
}