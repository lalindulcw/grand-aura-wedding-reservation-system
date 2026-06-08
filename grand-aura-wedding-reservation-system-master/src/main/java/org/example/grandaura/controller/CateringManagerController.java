package org.example.grandaura.controller;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.CateringManager;
import org.example.grandaura.entity.Menu;
import org.example.grandaura.entity.MenuPlan;
import org.example.grandaura.service.CateringManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for Catering Manager operations
 * Handles catering management, menu planning, and vendor coordination
 */
@Controller
@RequestMapping("/catering-manager")
public class CateringManagerController {

    private final CateringManagerService cateringManagerService;

    @Autowired
    public CateringManagerController(CateringManagerService cateringManagerService) {
        this.cateringManagerService = cateringManagerService;
    }

    /**
     * Catering Manager Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> cateringStats = cateringManagerService.getCateringStatistics();
        Map<String, Object> menuPlanningData = cateringManagerService.getMenuPlanningData();
        Map<String, Object> vendorData = cateringManagerService.getVendorManagementData();
        Map<String, Object> costAnalysis = cateringManagerService.getCostAnalysisData();
        
        model.addAttribute("cateringStats", cateringStats);
        model.addAttribute("menuPlanningData", menuPlanningData);
        model.addAttribute("vendorData", vendorData);
        model.addAttribute("costAnalysis", costAnalysis);
        
        return "catering-manager/dashboard";
    }

    /**
     * Menu Planning
     */
    @GetMapping("/menu-planning")
    public String menuPlanning(Model model) {
        List<Booking> upcomingBookings = cateringManagerService.getUpcomingBookings();
        Map<String, Object> menuData = cateringManagerService.getMenuPlanningData();
        
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("menuData", menuData);
        
        return "catering-manager/menu-planning";
    }

    /**
     * Vendor Management
     */
    @GetMapping("/vendors")
    public String vendorManagement(Model model) {
        Map<String, Object> vendorData = cateringManagerService.getVendorManagementData();
        
        model.addAttribute("vendorData", vendorData);
        
        return "catering-manager/vendors";
    }

    /**
     * Cost Analysis
     */
    @GetMapping("/cost-analysis")
    public String costAnalysis(Model model) {
        Map<String, Object> costData = cateringManagerService.getCostAnalysisData();
        List<Booking> upcomingBookings = cateringManagerService.getUpcomingBookings();
        
        model.addAttribute("costData", costData);
        model.addAttribute("upcomingBookings", upcomingBookings);
        
        return "catering-manager/cost-analysis";
    }

    /**
     * Booking Management
     */
    @GetMapping("/bookings")
    public String bookingManagement(Model model) {
        try {
            // First, let's try to get a simple count to test the database connection
            long bookingCount = cateringManagerService.getBookingCount();
            System.out.println("Total bookings in database: " + bookingCount);
            
            List<Booking> allBookings = cateringManagerService.getAllBookings();
            List<Booking> upcomingBookings = cateringManagerService.getUpcomingBookings();
            
            // Calculate statistics
            double avgGuestCount = allBookings.isEmpty() ? 0.0 : 
                allBookings.stream().mapToInt(Booking::getGuestCount).average().orElse(0.0);
            
            double totalRevenue = allBookings.stream()
                .mapToDouble(booking -> booking.getGuestCount() * 150.0)
                .sum();
            
            model.addAttribute("allBookings", allBookings);
            model.addAttribute("upcomingBookings", upcomingBookings);
            model.addAttribute("avgGuestCount", String.format("%.0f", avgGuestCount));
            model.addAttribute("totalRevenue", String.format("%.0f", totalRevenue));
            
            return "catering-manager/bookings";
        } catch (Exception e) {
            // Log the error and return a simple error page
            System.err.println("Error in bookingManagement: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading bookings: " + e.getMessage());
            return "catering-manager/error";
        }
    }

    /**
     * View specific booking details
     */
    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        List<Booking> bookings = cateringManagerService.getAllBookings();
        Optional<Booking> booking = bookings.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
        
        if (booking.isPresent()) {
            Booking bookingObj = booking.get();
            model.addAttribute("booking", bookingObj);
            
            return "catering-manager/bookingDetails";
        } else {
            return "redirect:/catering-manager/dashboard?error=Booking not found";
        }
    }

    /**
     * Test endpoint to verify controller is working
     */
    @GetMapping("/test")
    public String test() {
        return "Catering Manager Controller is working!";
    }

    /**
     * Edit catering details for a specific booking
     */
    @GetMapping("/catering-edit/{id}")
    public String editCateringForm(@PathVariable Long id, Model model) {
        System.out.println("Edit catering form called for booking ID: " + id);
        
        List<Booking> bookings = cateringManagerService.getAllBookings();
        System.out.println("Total bookings found: " + bookings.size());
        
        Optional<Booking> booking = bookings.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
        
        if (booking.isPresent()) {
            Booking bookingObj = booking.get();
            System.out.println("Booking found: " + bookingObj.getId());
            
            // Get available menu items
            List<Menu> availableMenus = cateringManagerService.getAllMenus();
            System.out.println("Available menus: " + availableMenus.size());
            
            model.addAttribute("booking", bookingObj);
            model.addAttribute("availableMenus", availableMenus);
            
            return "catering-manager/editCatering";
        } else {
            System.out.println("Booking not found for ID: " + id);
            return "redirect:/catering-manager/bookings?error=Booking not found";
        }
    }

    /**
     * Update catering details for a specific booking
     */
    @PostMapping("/catering-update/{id}")
    public String updateCatering(@PathVariable Long id,
                                @RequestParam(required = false) String cateringPackage,
                                @RequestParam(required = false) String dietaryRequirements,
                                @RequestParam(required = false) String specialCateringRequests,
                                @RequestParam(value = "selectedMenuItems", required = false) String[] selectedMenuItems,
                                Model model) {
        try {
            List<Booking> bookings = cateringManagerService.getAllBookings();
            Optional<Booking> bookingOpt = bookings.stream()
                    .filter(b -> b.getId().equals(id))
                    .findFirst();
            
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                
                // Update catering details
                if (cateringPackage != null) {
                    booking.setCateringPackage(cateringPackage);
                }
                if (dietaryRequirements != null) {
                    booking.setDietaryRequirements(dietaryRequirements);
                }
                if (specialCateringRequests != null) {
                    booking.setSpecialCateringRequests(specialCateringRequests);
                }
                
                // Process selected menu items
                if (selectedMenuItems != null && selectedMenuItems.length > 0) {
                    String menuItemsString = String.join(", ", selectedMenuItems);
                    booking.setSelectedMenuItems(menuItemsString);
                } else {
                    booking.setSelectedMenuItems(null);
                }
                
                // Save the updated booking
                cateringManagerService.updateBooking(booking);
                
                return "redirect:/catering-manager/bookings/" + id + "?success=Catering details updated successfully";
            } else {
                return "redirect:/catering-manager/bookings?error=Booking not found";
            }
        } catch (Exception e) {
            return "redirect:/catering-manager/bookings/" + id + "?error=Failed to update catering details: " + e.getMessage();
        }
    }

    /**
     * Catering Manager Profile
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            // Get the current catering manager from the database
            List<CateringManager> allManagers = cateringManagerService.getAllCateringManagers();
            CateringManager cateringManager;
            
            if (!allManagers.isEmpty()) {
                cateringManager = allManagers.get(0); // Get the first (and likely only) manager
            } else {
                // Create a default manager if none exists
                cateringManager = new CateringManager();
                cateringManager.setManagerName("Catering Manager");
                cateringManager.setEmail("catering@grandaura.com");
                cateringManager.setDepartment("Catering Services");
                cateringManager.setSpecialization("Wedding Catering");
                cateringManager.setPhoneNumber("+1-555-0123");
                cateringManager.setExperienceLevel("Senior");
                cateringManager.setCertifications("Certified Wedding Planner, Food Safety Certified");
                cateringManager.setEnabled(true);
                cateringManager.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye"); // BCrypt hash for "password"
                cateringManagerService.saveCateringManager(cateringManager);
            }
            
            model.addAttribute("cateringManager", cateringManager);
        } catch (Exception e) {
            // Fallback manager on error
            CateringManager fallbackManager = new CateringManager();
            fallbackManager.setManagerName("Catering Manager");
            fallbackManager.setEmail("catering@grandaura.com");
            fallbackManager.setDepartment("Catering Services");
            fallbackManager.setSpecialization("Wedding Catering");
            fallbackManager.setPhoneNumber("+1-555-0123");
            fallbackManager.setExperienceLevel("Senior");
            fallbackManager.setCertifications("Certified Wedding Planner, Food Safety Certified");
            model.addAttribute("cateringManager", fallbackManager);
            model.addAttribute("error", "Failed to load profile: " + e.getMessage());
        }
        return "catering-manager/profile";
    }

    /**
     * Update catering manager profile
     */
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String managerName,
                               @RequestParam String email,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) String specialization,
                               @RequestParam(required = false) String phoneNumber,
                               @RequestParam(required = false) String experienceLevel,
                               @RequestParam(required = false) String certifications,
                               Model model) {
        try {
            // Get all catering managers and find the one we want to update
            List<CateringManager> allManagers = cateringManagerService.getAllCateringManagers();
            CateringManager existingManager = null;
            
            // Find the existing manager (there should be only one in this demo)
            if (!allManagers.isEmpty()) {
                existingManager = allManagers.get(0); // Get the first (and likely only) manager
            }
            
            if (existingManager != null) {
                // Update existing manager with new values
                existingManager.setManagerName(managerName);
                existingManager.setEmail(email);
                existingManager.setDepartment(department);
                existingManager.setSpecialization(specialization);
                existingManager.setPhoneNumber(phoneNumber);
                existingManager.setExperienceLevel(experienceLevel);
                existingManager.setCertifications(certifications);
                // Keep existing password hash, enabled status
                
                cateringManagerService.saveCateringManager(existingManager);
                model.addAttribute("cateringManager", existingManager);
                model.addAttribute("success", "Profile updated successfully! If you changed your email, please logout and login again with your new email address.");
            } else {
                // Create new manager if none exists
                CateringManager newManager = new CateringManager();
                newManager.setManagerName(managerName);
                newManager.setEmail(email);
                newManager.setDepartment(department);
                newManager.setSpecialization(specialization);
                newManager.setPhoneNumber(phoneNumber);
                newManager.setExperienceLevel(experienceLevel);
                newManager.setCertifications(certifications);
                newManager.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye"); // BCrypt hash for "password"
                newManager.setEnabled(true);
                
                cateringManagerService.saveCateringManager(newManager);
                model.addAttribute("cateringManager", newManager);
                model.addAttribute("success", "Profile created successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            // Keep the original data on error
            List<CateringManager> allManagers = cateringManagerService.getAllCateringManagers();
            if (!allManagers.isEmpty()) {
                model.addAttribute("cateringManager", allManagers.get(0));
            } else {
                CateringManager fallbackManager = new CateringManager();
                fallbackManager.setManagerName("Catering Manager");
                fallbackManager.setEmail("catering@grandaura.com");
                fallbackManager.setDepartment("Catering Services");
                fallbackManager.setSpecialization("Wedding Catering");
                fallbackManager.setPhoneNumber("+1-555-0123");
                fallbackManager.setExperienceLevel("Senior");
                fallbackManager.setCertifications("Certified Wedding Planner, Food Safety Certified");
                model.addAttribute("cateringManager", fallbackManager);
            }
        }
        return "catering-manager/profile";
    }

    // Menu Management Endpoints

    /**
     * Menu Management Page
     */
    @GetMapping("/menus")
    public String menuManagement(Model model, 
                                @RequestParam(required = false) String category,
                                @RequestParam(required = false) String cuisine,
                                @RequestParam(required = false) String search) {
        List<Menu> menus;
        
        if (search != null && !search.trim().isEmpty()) {
            menus = cateringManagerService.searchMenusByName(search);
        } else if (category != null && !category.isEmpty()) {
            menus = cateringManagerService.getMenusByCategory(category);
        } else if (cuisine != null && !cuisine.isEmpty()) {
            menus = cateringManagerService.getMenusByCuisine(cuisine);
        } else {
            menus = cateringManagerService.getAllMenus();
        }
        
        Map<String, Object> menuStats = cateringManagerService.getMenuStatistics();
        List<String> categories = cateringManagerService.getAllCategories();
        List<String> cuisines = cateringManagerService.getAllCuisines();
        
        model.addAttribute("menus", menus);
        model.addAttribute("menuStats", menuStats);
        model.addAttribute("categories", categories);
        model.addAttribute("cuisines", cuisines);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedCuisine", cuisine);
        model.addAttribute("searchTerm", search);
        
        return "catering-manager/menus";
    }

    /**
     * Create Menu Item Page
     */
    @GetMapping("/menus/create")
    public String createMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("categories", cateringManagerService.getAllCategories());
        model.addAttribute("cuisines", cateringManagerService.getAllCuisines());
        return "catering-manager/menu-form";
    }

    /**
     * Edit Menu Item Page
     */
    @GetMapping("/menus/{id}/edit")
    public String editMenuForm(@PathVariable Long id, Model model) {
        Optional<Menu> menuOpt = cateringManagerService.getMenuById(id);
        if (menuOpt.isPresent()) {
            model.addAttribute("menu", menuOpt.get());
            model.addAttribute("categories", cateringManagerService.getAllCategories());
            model.addAttribute("cuisines", cateringManagerService.getAllCuisines());
            return "catering-manager/menu-form";
        } else {
            return "redirect:/catering-manager/menus?error=Menu not found";
        }
    }

    /**
     * Save Menu Item
     */
    @PostMapping("/menus/save")
    public String saveMenu(@RequestParam(required = false) Long id,
                          @RequestParam String name,
                          @RequestParam String description,
                          @RequestParam java.math.BigDecimal price,
                          @RequestParam String category,
                          @RequestParam String cuisine,
                          @RequestParam(required = false) String dietaryInfo,
                          @RequestParam Integer preparationTime,
                          @RequestParam(required = false) String allergens,
                          @RequestParam(defaultValue = "true") Boolean isAvailable) {
        try {
            Menu menu;
            if (id != null) {
                // Update existing menu
                Optional<Menu> existingMenuOpt = cateringManagerService.getMenuById(id);
                if (existingMenuOpt.isPresent()) {
                    menu = existingMenuOpt.get();
                } else {
                    return "redirect:/catering-manager/menus?error=Menu not found";
                }
            } else {
                // Create new menu
                menu = new Menu();
            }
            
            menu.setName(name);
            menu.setDescription(description);
            menu.setPrice(price);
            menu.setCategory(category);
            menu.setCuisine(cuisine);
            menu.setDietaryInfo(dietaryInfo);
            menu.setPreparationTime(preparationTime);
            menu.setAllergens(allergens);
            menu.setIsAvailable(isAvailable);
            
            cateringManagerService.saveMenu(menu);
            return "redirect:/catering-manager/menus?success=Menu saved successfully";
        } catch (Exception e) {
            return "redirect:/catering-manager/menus?error=Error saving menu: " + e.getMessage();
        }
    }

    /**
     * Delete Menu Item
     */
    @PostMapping("/menus/{id}/delete")
    public String deleteMenu(@PathVariable Long id) {
        try {
            cateringManagerService.deleteMenu(id);
            return "redirect:/catering-manager/menus?success=Menu deleted successfully";
        } catch (Exception e) {
            return "redirect:/catering-manager/menus?error=Error deleting menu: " + e.getMessage();
        }
    }

    /**
     * Toggle Menu Availability
     */
    @PostMapping("/menus/{id}/toggle")
    public String toggleMenuAvailability(@PathVariable Long id) {
        try {
            cateringManagerService.toggleMenuAvailability(id);
            return "redirect:/catering-manager/menus?success=Menu availability updated";
        } catch (Exception e) {
            return "redirect:/catering-manager/menus?error=Error updating menu: " + e.getMessage();
        }
    }

    // Menu Plan Management Endpoints

    /**
     * Menu Plans Templates List Page
     */
    @GetMapping("/menu-plans")
    public String menuPlans(Model model, @RequestParam(required = false) String category) {
        List<MenuPlan> menuPlans;
        
        if (category != null && !category.isEmpty()) {
            menuPlans = cateringManagerService.getMenuPlansByCategory(category);
        } else {
            menuPlans = cateringManagerService.getAllMenuPlans();
        }
        
        Map<String, Object> menuPlanStats = cateringManagerService.getMenuPlanStatistics();
        
        model.addAttribute("menuPlans", menuPlans);
        model.addAttribute("menuPlanStats", menuPlanStats);
        model.addAttribute("selectedCategory", category);
        
        return "catering-manager/menu-plans";
    }

    /**
     * Create Menu Plan Template Form
     */
    @GetMapping("/menu-plans/create")
    public String createMenuPlanForm(Model model) {
        model.addAttribute("menuPlan", new MenuPlan());
        return "catering-manager/menu-plan-form";
    }

    /**
     * Edit Menu Plan Template Form
     */
    @GetMapping("/menu-plans/{id}/edit")
    public String editMenuPlanForm(@PathVariable Long id, Model model) {
        Optional<MenuPlan> menuPlanOpt = cateringManagerService.getMenuPlanById(id);
        if (menuPlanOpt.isPresent()) {
            model.addAttribute("menuPlan", menuPlanOpt.get());
            return "catering-manager/menu-plan-form";
        } else {
            return "redirect:/catering-manager/menu-plans?error=Menu plan template not found";
        }
    }

    /**
     * Save Menu Plan Template
     */
    @PostMapping("/menu-plans/save")
    public String saveMenuPlan(@RequestParam(required = false) Long id,
                              @RequestParam String planName,
                              @RequestParam(required = false) String description,
                              @RequestParam Double pricePerGuest,
                              @RequestParam String category,
                              @RequestParam(required = false) String appetizers,
                              @RequestParam(required = false) String mainCourses,
                              @RequestParam(required = false) String desserts,
                              @RequestParam(required = false) String beverages,
                              @RequestParam(required = false) String dietaryInfo,
                              @RequestParam(required = false) String specialNotes,
                              @RequestParam(defaultValue = "true") Boolean isActive,
                              RedirectAttributes redirectAttributes) {
        try {
            MenuPlan menuPlan;
            
            if (id != null) {
                // Update existing menu plan template
                Optional<MenuPlan> existingPlanOpt = cateringManagerService.getMenuPlanById(id);
                if (existingPlanOpt.isPresent()) {
                    menuPlan = existingPlanOpt.get();
                } else {
                    redirectAttributes.addFlashAttribute("error", "Menu plan template not found");
                    return "redirect:/catering-manager/menu-plans";
                }
            } else {
                // Create new menu plan template
                menuPlan = new MenuPlan();
                menuPlan.setCreatedBy("Catering Manager");
            }
            
            menuPlan.setPlanName(planName);
            menuPlan.setDescription(description);
            menuPlan.setPricePerGuest(pricePerGuest);
            menuPlan.setCategory(category);
            menuPlan.setAppetizers(appetizers);
            menuPlan.setMainCourses(mainCourses);
            menuPlan.setDesserts(desserts);
            menuPlan.setBeverages(beverages);
            menuPlan.setDietaryInfo(dietaryInfo);
            menuPlan.setSpecialNotes(specialNotes);
            menuPlan.setIsActive(isActive);
            
            MenuPlan savedPlan = cateringManagerService.saveMenuPlan(menuPlan);
            redirectAttributes.addFlashAttribute("success", "Menu plan template saved successfully");
            return "redirect:/catering-manager/menu-plans/" + savedPlan.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving menu plan template: " + e.getMessage());
            return "redirect:/catering-manager/menu-plans";
        }
    }

    /**
     * View Menu Plan Template Details
     */
    @GetMapping("/menu-plans/{id}")
    public String viewMenuPlan(@PathVariable Long id, Model model) {
        Optional<MenuPlan> menuPlanOpt = cateringManagerService.getMenuPlanById(id);
        if (menuPlanOpt.isPresent()) {
            model.addAttribute("menuPlan", menuPlanOpt.get());
            return "catering-manager/menu-plan-details";
        } else {
            return "redirect:/catering-manager/menu-plans?error=Menu plan template not found";
        }
    }

    /**
     * Delete Menu Plan Template
     */
    @PostMapping("/menu-plans/{id}/delete")
    public String deleteMenuPlan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cateringManagerService.deleteMenuPlan(id);
            redirectAttributes.addFlashAttribute("success", "Menu plan template deleted successfully");
            return "redirect:/catering-manager/menu-plans";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting menu plan template: " + e.getMessage());
            return "redirect:/catering-manager/menu-plans";
        }
    }

    /**
     * Toggle Menu Plan Template Status (Active/Inactive)
     */
    @PostMapping("/menu-plans/{id}/toggle")
    public String toggleMenuPlanStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cateringManagerService.toggleMenuPlanStatus(id);
            redirectAttributes.addFlashAttribute("success", "Menu plan template status updated");
            return "redirect:/catering-manager/menu-plans/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating status: " + e.getMessage());
            return "redirect:/catering-manager/menu-plans/" + id;
        }
    }
}
