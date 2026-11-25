package com.mining.koscheithedeathless.design.devclassification.repository;

import com.mining.koscheithedeathless.design.devclassification.PropertyStability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyStabilityRepository extends JpaRepository<PropertyStability, Long> {}