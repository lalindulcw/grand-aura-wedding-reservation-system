package org.example.grandaura.repository;

import org.example.grandaura.entity.SystemAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemAdministratorRepository extends JpaRepository<SystemAdministrator, Long> {
    Optional<SystemAdministrator> findByEmail(String email);
}
