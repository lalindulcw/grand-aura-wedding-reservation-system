package org.example.grandaura.repository;

import org.example.grandaura.entity.HotelOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for HotelOwner entity
 * Provides data access methods for hotel owner management
 */
@Repository
public interface HotelOwnerRepository extends JpaRepository<HotelOwner, Long> {
    
    /**
     * Find hotel owner by email
     * @param email the email address
     * @return Optional containing the hotel owner if found
     */
    Optional<HotelOwner> findByEmail(String email);
    
    /**
     * Check if hotel owner exists by email
     * @param email the email address
     * @return true if hotel owner exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find hotel owner by email and enabled status
     * @param email the email address
     * @param enabled the enabled status
     * @return Optional containing the hotel owner if found
     */
    Optional<HotelOwner> findByEmailAndEnabled(String email, boolean enabled);
    
    /**
     * Count total hotel owners
     * @return total number of hotel owners
     */
    @Query("SELECT COUNT(h) FROM HotelOwner h WHERE h.enabled = true")
    long countActiveHotelOwners();
}
