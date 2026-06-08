package org.example.grandaura.repository;

import org.example.grandaura.entity.MenuPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MenuPlan entity
 * Provides data access methods for menu plan template operations
 */
@Repository
public interface MenuPlanRepository extends JpaRepository<MenuPlan, Long> {

    /**
     * Find all active menu plans (available for customers)
     */
    List<MenuPlan> findByIsActiveTrue();

    /**
     * Find menu plans by category
     */
    List<MenuPlan> findByCategory(String category);

    /**
     * Find active menu plans by category
     */
    List<MenuPlan> findByCategoryAndIsActiveTrue(String category);

    /**
     * Find all menu plans ordered by plan name
     */
    @Query("SELECT mp FROM MenuPlan mp ORDER BY mp.planName ASC")
    List<MenuPlan> findAllOrderByPlanName();

    /**
     * Count active menu plans
     */
    long countByIsActiveTrue();

    /**
     * Count menu plans by category
     */
    long countByCategory(String category);
}

