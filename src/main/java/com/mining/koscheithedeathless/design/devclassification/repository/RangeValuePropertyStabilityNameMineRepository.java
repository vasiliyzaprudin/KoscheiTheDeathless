package com.mining.koscheithedeathless.design.devclassification.repository;

import com.mining.koscheithedeathless.design.devclassification.RangeValuePropertyStabilityNameMine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RangeValuePropertyStabilityNameMineRepository extends JpaRepository<RangeValuePropertyStabilityNameMine, Long> {
    List<RangeValuePropertyStabilityNameMine> findByGeologicalStabilityClassMineGeologicalStabilityClassMineId(Long classMineId);
}
