package org.example.grandaura.repository;

import org.example.grandaura.entity.FrontDeskOfficer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for FrontDeskOfficer entity
 * Provides CRUD operations and custom query methods
 */
@Repository
public interface FrontDeskOfficerRepository extends JpaRepository<FrontDeskOfficer, Long> {
    
    /**
     * Find a front desk officer by email
     * Used for authentication
     */
    Optional<FrontDeskOfficer> findByEmail(String email);
    
    /**
     * Find a front desk officer by employee ID
     */
    Optional<FrontDeskOfficer> findByEmployeeId(String employeeId);
}

