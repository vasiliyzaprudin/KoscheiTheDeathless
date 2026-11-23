package com.mining.koscheithedeathless.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MineRepository extends JpaRepository<Mine, Long> {
}
