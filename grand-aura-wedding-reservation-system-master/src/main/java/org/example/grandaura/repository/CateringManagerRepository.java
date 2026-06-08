package org.example.grandaura.repository;

import org.example.grandaura.entity.CateringManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CateringManagerRepository extends JpaRepository<CateringManager, Long> {
    Optional<CateringManager> findByEmail(String email);
}

