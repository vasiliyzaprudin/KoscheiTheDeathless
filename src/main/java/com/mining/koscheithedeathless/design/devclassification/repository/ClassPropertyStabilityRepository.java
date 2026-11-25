package com.mining.koscheithedeathless.design.devclassification.repository;

import com.mining.koscheithedeathless.design.devclassification.ClassPropertyStability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassPropertyStabilityRepository extends JpaRepository<ClassPropertyStability, Long> {

    // Найти все свойства, привязанные к конкретному классу
    List<ClassPropertyStability> findByGeologicalStabilityClassGeologicalStabilityClassId(Long classId);
}
