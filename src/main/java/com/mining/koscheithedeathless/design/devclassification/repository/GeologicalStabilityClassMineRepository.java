package com.mining.koscheithedeathless.design.devclassification.repository;

import com.mining.koscheithedeathless.design.devclassification.GeologicalStabilityClassMine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeologicalStabilityClassMineRepository extends JpaRepository<GeologicalStabilityClassMine, Long> {
    List<GeologicalStabilityClassMine> findByMineId(Long mineId);
}
