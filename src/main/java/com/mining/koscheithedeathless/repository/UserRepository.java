package com.mining.koscheithedeathless.repository;

import com.mining.koscheithedeathless.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
