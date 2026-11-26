package com.mining.koscheithedeathless.design.devclassification.repository;

import com.mining.koscheithedeathless.design.devclassification.RangeValuePropertyStabilityNameMine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RangeValuePropertyStabilityNameMineRepository
        extends JpaRepository<RangeValuePropertyStabilityNameMine, Long> {

    List<RangeValuePropertyStabilityNameMine> findByGeologicalStabilityClassMine_GeologicalStabilityClassMineId(Long classMineId);

    Optional<RangeValuePropertyStabilityNameMine> findByGeologicalStabilityClassMine_GeologicalStabilityClassMineIdAndPropertyStability_PropertyStabilityId(
            Long classMineId, Long propertyId);
}