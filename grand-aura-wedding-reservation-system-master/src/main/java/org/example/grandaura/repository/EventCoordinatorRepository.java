package org.example.grandaura.repository;

import org.example.grandaura.entity.EventCoordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for EventCoordinator entity
 * Provides database operations for event coordinators
 */
@Repository
public interface EventCoordinatorRepository extends JpaRepository<EventCoordinator, Long> {
    
    /**
     * Find event coordinator by email
     * @param email the email address
     * @return Optional containing the event coordinator if found
     */
    Optional<EventCoordinator> findByEmail(String email);
    
    /**
     * Check if event coordinator exists by email
     * @param email the email address
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}

