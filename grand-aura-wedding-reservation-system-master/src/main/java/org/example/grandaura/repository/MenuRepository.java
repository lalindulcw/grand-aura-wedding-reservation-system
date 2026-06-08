package org.example.grandaura.repository;

import org.example.grandaura.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    // Find menus by category
    List<Menu> findByCategory(String category);
    
    // Find menus by cuisine
    List<Menu> findByCuisine(String cuisine);
    
    // Find available menus
    List<Menu> findByIsAvailableTrue();
    
    // Find menus by category and availability
    List<Menu> findByCategoryAndIsAvailableTrue(String category);
    
    // Find menus by cuisine and availability
    List<Menu> findByCuisineAndIsAvailableTrue(String cuisine);
    
    // Search menus by name (case insensitive)
    @Query("SELECT m FROM Menu m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Menu> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Find menus by dietary info
    @Query("SELECT m FROM Menu m WHERE m.dietaryInfo LIKE %:dietaryInfo%")
    List<Menu> findByDietaryInfoContaining(@Param("dietaryInfo") String dietaryInfo);
    
    // Find menus by price range
    @Query("SELECT m FROM Menu m WHERE m.price BETWEEN :minPrice AND :maxPrice")
    List<Menu> findByPriceBetween(@Param("minPrice") java.math.BigDecimal minPrice, 
                                  @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    // Count menus by category
    @Query("SELECT COUNT(m) FROM Menu m WHERE m.category = :category")
    Long countByCategory(@Param("category") String category);
    
    // Get all unique categories
    @Query("SELECT DISTINCT m.category FROM Menu m ORDER BY m.category")
    List<String> findDistinctCategories();
    
    // Get all unique cuisines
    @Query("SELECT DISTINCT m.cuisine FROM Menu m ORDER BY m.cuisine")
    List<String> findDistinctCuisines();
}

