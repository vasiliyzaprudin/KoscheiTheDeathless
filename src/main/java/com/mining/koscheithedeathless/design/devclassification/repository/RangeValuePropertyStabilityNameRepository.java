package com.mining.koscheithedeathless.design.devclassification.repository;

import com.mining.koscheithedeathless.design.devclassification.RangeValuePropertyStabilityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RangeValuePropertyStabilityNameRepository
        extends JpaRepository<RangeValuePropertyStabilityName, Long> {

    List<RangeValuePropertyStabilityName> findByPropertyStability_PropertyStabilityId(Long propertyId);
}