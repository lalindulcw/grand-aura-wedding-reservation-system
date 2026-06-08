package org.example.grandaura.service;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.CateringManager;
import org.example.grandaura.entity.Menu;
import org.example.grandaura.entity.MenuPlan;
import org.example.grandaura.repository.BookingRepository;
import org.example.grandaura.repository.CateringManagerRepository;
import org.example.grandaura.repository.MenuRepository;
import org.example.grandaura.repository.MenuPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for Catering Manager operations
 * Provides catering management, menu planning, and vendor coordination
 */
@Service
public class CateringManagerService {

    private final CateringManagerRepository cateringManagerRepository;
    private final BookingRepository bookingRepository;
    private final MenuRepository menuRepository;
    private final MenuPlanRepository menuPlanRepository;

    @Autowired
    public CateringManagerService(CateringManagerRepository cateringManagerRepository,
                                 BookingRepository bookingRepository,
                                 MenuRepository menuRepository,
                                 MenuPlanRepository menuPlanRepository) {
        this.cateringManagerRepository = cateringManagerRepository;
        this.bookingRepository = bookingRepository;
        this.menuRepository = menuRepository;
        this.menuPlanRepository = menuPlanRepository;
    }

    // Catering Manager Management Methods

    /**
     * Save or update catering manager
     */
    public CateringManager saveCateringManager(CateringManager cateringManager) {
        return cateringManagerRepository.save(cateringManager);
    }

    /**
     * Find catering manager by email
     */
    public Optional<CateringManager> findByEmail(String email) {
        return cateringManagerRepository.findByEmail(email);
    }

    /**
     * Get all catering managers
     */
    public List<CateringManager> getAllCateringManagers() {
        return cateringManagerRepository.findAll();
    }

    /**
     * Delete catering manager by ID
     */
    public void deleteCateringManager(Long id) {
        cateringManagerRepository.deleteById(id);
    }

    // Catering Management Methods

    /**
     * Get all bookings for catering coordination
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public long getBookingCount() {
        return bookingRepository.count();
    }

    /**
     * Get upcoming bookings for catering planning
     */
    public List<Booking> getUpcomingBookings() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getWeddingDate().isAfter(today) || booking.getWeddingDate().isEqual(today))
                .toList();
    }

    /**
     * Update a booking (for catering manager to edit catering details)
     */
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    /**
     * Get catering statistics
     */
    public Map<String, Object> getCateringStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Booking> allBookings = getAllBookings();
        List<Booking> upcomingBookings = getUpcomingBookings();
        
        stats.put("totalBookings", allBookings.size());
        stats.put("upcomingBookings", upcomingBookings.size());
        stats.put("completedBookings", allBookings.size() - upcomingBookings.size());
        
        // Calculate average guest count
        double avgGuests = allBookings.stream()
                .mapToInt(Booking::getGuestCount)
                .average()
                .orElse(0.0);
        stats.put("averageGuestCount", Math.round(avgGuests));
        
        // Calculate total revenue potential
        double totalRevenue = allBookings.stream()
                .mapToDouble(booking -> booking.getGuestCount() * 150.0) // Assuming $150 per guest
                .sum();
        stats.put("totalRevenuePotential", totalRevenue);
        
        return stats;
    }

    /**
     * Get menu planning data
     */
    public Map<String, Object> getMenuPlanningData() {
        Map<String, Object> data = new HashMap<>();
        
        List<Booking> upcomingBookings = getUpcomingBookings();
        
        // Group by venue for menu planning
        Map<String, Long> bookingsByVenue = upcomingBookings.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        booking -> booking.getPreferredVenue() != null ? booking.getPreferredVenue() : booking.getVenue(),
                        java.util.stream.Collectors.counting()
                ));
        
        data.put("bookingsByVenue", bookingsByVenue);
        data.put("totalUpcomingEvents", upcomingBookings.size());
        
        // Calculate dietary requirements (mock data)
        data.put("vegetarianRequests", upcomingBookings.size() * 0.3);
        data.put("veganRequests", upcomingBookings.size() * 0.1);
        data.put("glutenFreeRequests", upcomingBookings.size() * 0.2);
        data.put("halalRequests", upcomingBookings.size() * 0.15);
        
        return data;
    }

    /**
     * Get vendor management data
     */
    public Map<String, Object> getVendorManagementData() {
        Map<String, Object> data = new HashMap<>();
        
        // Mock vendor data
        data.put("activeVendors", 12);
        data.put("pendingOrders", 8);
        data.put("completedOrders", 45);
        data.put("averageRating", 4.7);
        
        // Vendor categories
        Map<String, Integer> vendorsByCategory = new HashMap<>();
        vendorsByCategory.put("Catering", 4);
        vendorsByCategory.put("Beverages", 3);
        vendorsByCategory.put("Desserts", 2);
        vendorsByCategory.put("Equipment", 3);
        
        data.put("vendorsByCategory", vendorsByCategory);
        
        return data;
    }

    /**
     * Get cost analysis data
     */
    public Map<String, Object> getCostAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        
        List<Booking> upcomingBookings = getUpcomingBookings();
        
        // Calculate costs
        double totalFoodCost = upcomingBookings.stream()
                .mapToDouble(booking -> booking.getGuestCount() * 75.0) // $75 per guest for food
                .sum();
        
        double totalBeverageCost = upcomingBookings.stream()
                .mapToDouble(booking -> booking.getGuestCount() * 25.0) // $25 per guest for beverages
                .sum();
        
        double totalServiceCost = upcomingBookings.stream()
                .mapToDouble(booking -> booking.getGuestCount() * 50.0) // $50 per guest for service
                .sum();
        
        data.put("totalFoodCost", totalFoodCost);
        data.put("totalBeverageCost", totalBeverageCost);
        data.put("totalServiceCost", totalServiceCost);
        data.put("totalCateringCost", totalFoodCost + totalBeverageCost + totalServiceCost);
        
        return data;
    }

    /**
     * Update catering manager
     */
    public void updateCateringManager(Long id, String managerName, String email, String department, boolean enabled) {
        Optional<CateringManager> managerOpt = cateringManagerRepository.findById(id);
        if (managerOpt.isPresent()) {
            CateringManager manager = managerOpt.get();
            manager.setManagerName(managerName);
            manager.setEmail(email);
            manager.setDepartment(department);
            manager.setEnabled(enabled);
            cateringManagerRepository.save(manager);
        }
    }

    /**
     * Toggle catering manager status
     */
    public void toggleCateringManager(Long id) {
        Optional<CateringManager> managerOpt = cateringManagerRepository.findById(id);
        if (managerOpt.isPresent()) {
            CateringManager manager = managerOpt.get();
            manager.setEnabled(!manager.isEnabled());
            cateringManagerRepository.save(manager);
        }
    }

    // Menu Management Methods

    /**
     * Get all menu items
     */
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    /**
     * Get menu by ID
     */
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    /**
     * Save or update menu item
     */
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    /**
     * Delete menu item
     */
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    /**
     * Get menus by category
     */
    public List<Menu> getMenusByCategory(String category) {
        return menuRepository.findByCategoryAndIsAvailableTrue(category);
    }

    /**
     * Get menus by cuisine
     */
    public List<Menu> getMenusByCuisine(String cuisine) {
        return menuRepository.findByCuisineAndIsAvailableTrue(cuisine);
    }

    /**
     * Get available menus only
     */
    public List<Menu> getAvailableMenus() {
        return menuRepository.findByIsAvailableTrue();
    }

    /**
     * Search menus by name
     */
    public List<Menu> searchMenusByName(String name) {
        return menuRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Get all unique categories
     */
    public List<String> getAllCategories() {
        return menuRepository.findDistinctCategories();
    }

    /**
     * Get all unique cuisines
     */
    public List<String> getAllCuisines() {
        return menuRepository.findDistinctCuisines();
    }

    /**
     * Toggle menu availability
     */
    public void toggleMenuAvailability(Long id) {
        Optional<Menu> menuOpt = menuRepository.findById(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setIsAvailable(!menu.getIsAvailable());
            menuRepository.save(menu);
        }
    }

    /**
     * Get menu statistics
     */
    public Map<String, Object> getMenuStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Menu> allMenus = menuRepository.findAll();
        List<Menu> availableMenus = menuRepository.findByIsAvailableTrue();
        
        stats.put("totalMenus", allMenus.size());
        stats.put("availableMenus", availableMenus.size());
        stats.put("unavailableMenus", allMenus.size() - availableMenus.size());
        
        // Count by category
        Map<String, Long> categoryCount = new HashMap<>();
        for (String category : menuRepository.findDistinctCategories()) {
            categoryCount.put(category, menuRepository.countByCategory(category));
        }
        stats.put("categoryCount", categoryCount);
        
        return stats;
    }

    // Menu Plan Template Management Methods

    /**
     * Get all menu plan templates
     */
    public List<MenuPlan> getAllMenuPlans() {
        return menuPlanRepository.findAllOrderByPlanName();
    }

    /**
     * Get active menu plan templates (available for customers)
     */
    public List<MenuPlan> getActiveMenuPlans() {
        return menuPlanRepository.findByIsActiveTrue();
    }

    /**
     * Get menu plan by ID
     */
    public Optional<MenuPlan> getMenuPlanById(Long id) {
        return menuPlanRepository.findById(id);
    }

    /**
     * Save or update menu plan template
     */
    public MenuPlan saveMenuPlan(MenuPlan menuPlan) {
        menuPlan.setLastModifiedDate(LocalDate.now());
        return menuPlanRepository.save(menuPlan);
    }

    /**
     * Create a new menu plan template
     */
    public MenuPlan createMenuPlan(MenuPlan menuPlan) {
        menuPlan.setCreatedDate(LocalDate.now());
        menuPlan.setLastModifiedDate(LocalDate.now());
        if (menuPlan.getIsActive() == null) {
            menuPlan.setIsActive(true);
        }
        return menuPlanRepository.save(menuPlan);
    }

    /**
     * Delete menu plan template
     */
    public void deleteMenuPlan(Long id) {
        menuPlanRepository.deleteById(id);
    }

    /**
     * Get menu plans by category
     */
    public List<MenuPlan> getMenuPlansByCategory(String category) {
        return menuPlanRepository.findByCategory(category);
    }

    /**
     * Toggle menu plan active status
     */
    public void toggleMenuPlanStatus(Long id) {
        Optional<MenuPlan> menuPlanOpt = menuPlanRepository.findById(id);
        if (menuPlanOpt.isPresent()) {
            MenuPlan menuPlan = menuPlanOpt.get();
            menuPlan.setIsActive(!menuPlan.getIsActive());
            menuPlan.setLastModifiedDate(LocalDate.now());
            menuPlanRepository.save(menuPlan);
        }
    }

    /**
     * Get menu plan template statistics
     */
    public Map<String, Object> getMenuPlanStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<MenuPlan> allPlans = menuPlanRepository.findAll();
        
        stats.put("totalPlans", allPlans.size());
        stats.put("activePlans", menuPlanRepository.countByIsActiveTrue());
        stats.put("inactivePlans", allPlans.size() - menuPlanRepository.countByIsActiveTrue());
        stats.put("classicPlans", menuPlanRepository.countByCategory("Classic"));
        stats.put("premiumPlans", menuPlanRepository.countByCategory("Premium"));
        stats.put("luxuryPlans", menuPlanRepository.countByCategory("Luxury"));
        
        return stats;
    }
}
