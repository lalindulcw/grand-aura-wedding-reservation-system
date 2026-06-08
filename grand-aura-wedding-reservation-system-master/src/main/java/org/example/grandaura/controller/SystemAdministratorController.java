package org.example.grandaura.controller;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.EventCoordinator;
import org.example.grandaura.entity.HotelOwner;
import org.example.grandaura.entity.SystemAdministrator;
import org.example.grandaura.entity.UserAccount;
import org.example.grandaura.entity.CateringManager;
import org.example.grandaura.service.SystemAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for System Administrator operations
 * Handles system-wide management, user administration, and analytics
 */
@Controller
@RequestMapping("/system-admin")
public class SystemAdministratorController {

    private final SystemAdministratorService systemAdministratorService;

    @Autowired
    public SystemAdministratorController(SystemAdministratorService systemAdministratorService) {
        this.systemAdministratorService = systemAdministratorService;
    }

    /**
     * System Administrator Dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> systemStats = systemAdministratorService.getSystemStatistics();
        Map<String, Object> databaseHealth = systemAdministratorService.getDatabaseHealth();
        Map<String, Object> systemPerformance = systemAdministratorService.getSystemPerformance();
        Map<String, Object> userActivity = systemAdministratorService.getUserActivity();
        
        model.addAttribute("systemStats", systemStats);
        model.addAttribute("databaseHealth", databaseHealth);
        model.addAttribute("systemPerformance", systemPerformance);
        model.addAttribute("userActivity", userActivity);
        
        return "system-admin/dashboard";
    }

    /**
     * User Management
     */
    @GetMapping("/users")
    public String userManagement(Model model) {
        List<UserAccount> customers = systemAdministratorService.getAllUserAccounts();
        List<HotelOwner> hotelOwners = systemAdministratorService.getAllHotelOwners();
        List<EventCoordinator> eventCoordinators = systemAdministratorService.getAllEventCoordinators();
        List<SystemAdministrator> systemAdmins = systemAdministratorService.getAllSystemAdministrators();
        List<CateringManager> cateringManagers = systemAdministratorService.getAllCateringManagers();
        
        model.addAttribute("customers", customers);
        model.addAttribute("hotelOwners", hotelOwners);
        model.addAttribute("eventCoordinators", eventCoordinators);
        model.addAttribute("systemAdmins", systemAdmins);
        model.addAttribute("cateringManagers", cateringManagers);
        
        return "system-admin/users";
    }

    /**
     * Show Add User Form
     */
    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        return "system-admin/addUser";
    }

    /**
     * Create New User
     */
    @PostMapping("/users/create")
    public String createUser(
            @RequestParam String userType,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String coordinatorName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String adminName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String managerName,
            @RequestParam(required = false) String cateringDepartment,
            @RequestParam(required = false) String officerName,
            @RequestParam(required = false) String shift,
            @RequestParam(required = false) String fdDepartment,
            @RequestParam(required = false) String employeeId,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        try {
            systemAdministratorService.createUser(
                userType, email, password, 
                ownerName, hotelName, description,
                coordinatorName, phoneNumber, specialization,
                adminName, department,
                managerName, cateringDepartment,
                officerName, shift, fdDepartment, employeeId
            );
            
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
            return "redirect:/system-admin/users";
            
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create user: " + e.getMessage());
            return "system-admin/addUser";
        }
    }

    /**
     * System Settings
     */
    @GetMapping("/settings")
    public String systemSettings(Model model) {
        Map<String, Object> systemStats = systemAdministratorService.getSystemStatistics();
        model.addAttribute("systemStats", systemStats);
        return "system-admin/settings";
    }

    /**
     * Global Analytics
     */
    @GetMapping("/analytics")
    public String analytics(Model model) {
        Map<String, Object> systemStats = systemAdministratorService.getSystemStatistics();
        Map<String, Object> databaseHealth = systemAdministratorService.getDatabaseHealth();
        Map<String, Object> systemPerformance = systemAdministratorService.getSystemPerformance();
        Map<String, Object> userActivity = systemAdministratorService.getUserActivity();
        
        model.addAttribute("systemStats", systemStats);
        model.addAttribute("databaseHealth", databaseHealth);
        model.addAttribute("systemPerformance", systemPerformance);
        model.addAttribute("userActivity", userActivity);
        
        return "system-admin/analytics";
    }

    /**
     * Database Management
     */
    @GetMapping("/database")
    public String databaseManagement(Model model) {
        Map<String, Object> databaseHealth = systemAdministratorService.getDatabaseHealth();
        Map<String, Long> userCounts = systemAdministratorService.getUserCounts();
        
        model.addAttribute("databaseHealth", databaseHealth);
        model.addAttribute("userCounts", userCounts);
        
        return "system-admin/database";
    }

    /**
     * System Administrator Profile
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            // Get the current system administrator from the database
            List<SystemAdministrator> allAdmins = systemAdministratorService.getAllSystemAdministrators();
            SystemAdministrator systemAdmin;
            
            if (!allAdmins.isEmpty()) {
                systemAdmin = allAdmins.get(0); // Get the first (and likely only) admin
            } else {
                // Create a default admin if none exists
                systemAdmin = new SystemAdministrator();
                systemAdmin.setAdministratorName("Admin User");
                systemAdmin.setEmail("admin@grandaura.com");
                systemAdmin.setDepartment("IT Administration");
                systemAdmin.setRole("System Administrator");
                systemAdmin.setPhoneNumber("+1-555-0000");
                systemAdmin.setPermissions("ALL");
                systemAdmin.setSuperAdmin(true);
                systemAdmin.setEnabled(true);
                systemAdmin.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye"); // BCrypt hash for "password"
                systemAdministratorService.saveSystemAdministrator(systemAdmin);
            }
            
            model.addAttribute("systemAdministrator", systemAdmin);
        } catch (Exception e) {
            // Fallback admin on error
            SystemAdministrator fallbackAdmin = new SystemAdministrator();
            fallbackAdmin.setAdministratorName("Admin User");
            fallbackAdmin.setEmail("admin@grandaura.com");
            fallbackAdmin.setDepartment("IT Administration");
            fallbackAdmin.setRole("System Administrator");
            fallbackAdmin.setPhoneNumber("+1-555-0000");
            fallbackAdmin.setPermissions("ALL");
            fallbackAdmin.setSuperAdmin(true);
            model.addAttribute("systemAdministrator", fallbackAdmin);
            model.addAttribute("error", "Failed to load profile: " + e.getMessage());
        }
        return "system-admin/profile";
    }

    /**
     * Update system administrator profile
     */
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String administratorName,
                               @RequestParam String email,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) String role,
                               @RequestParam(required = false) String phoneNumber,
                               @RequestParam(required = false) String permissions,
                               Model model) {
        
        try {
            // Get all system administrators and find the one we want to update
            List<SystemAdministrator> allAdmins = systemAdministratorService.getAllSystemAdministrators();
            SystemAdministrator existingAdmin = null;
            
            // Find the existing admin (there should be only one in this demo)
            if (!allAdmins.isEmpty()) {
                existingAdmin = allAdmins.get(0); // Get the first (and likely only) admin
            }
            
            if (existingAdmin != null) {
                // Update existing admin with new values
                existingAdmin.setAdministratorName(administratorName);
                existingAdmin.setEmail(email);
                existingAdmin.setDepartment(department);
                existingAdmin.setRole(role);
                existingAdmin.setPhoneNumber(phoneNumber);
                existingAdmin.setPermissions(permissions);
                // Keep existing password hash, enabled status, and superAdmin status
                
                systemAdministratorService.saveSystemAdministrator(existingAdmin);
                model.addAttribute("systemAdministrator", existingAdmin);
                model.addAttribute("success", "Profile updated successfully! If you changed your email, please logout and login again with your new email address.");
            } else {
                // Create new admin if none exists
                SystemAdministrator newAdmin = new SystemAdministrator();
                newAdmin.setAdministratorName(administratorName);
                newAdmin.setEmail(email);
                newAdmin.setDepartment(department);
                newAdmin.setRole(role);
                newAdmin.setPhoneNumber(phoneNumber);
                newAdmin.setPermissions(permissions);
                newAdmin.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye"); // BCrypt hash for "password"
                newAdmin.setEnabled(true);
                newAdmin.setSuperAdmin(true);
                
                systemAdministratorService.saveSystemAdministrator(newAdmin);
                model.addAttribute("systemAdministrator", newAdmin);
                model.addAttribute("success", "Profile created successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            // Keep the original data on error
            List<SystemAdministrator> allAdmins = systemAdministratorService.getAllSystemAdministrators();
            if (!allAdmins.isEmpty()) {
                model.addAttribute("systemAdministrator", allAdmins.get(0));
            } else {
                SystemAdministrator fallbackAdmin = new SystemAdministrator();
                fallbackAdmin.setAdministratorName("System Administrator");
                fallbackAdmin.setEmail("admin@grandaura.com");
                fallbackAdmin.setDepartment("IT Administration");
                fallbackAdmin.setRole("System Administrator");
                fallbackAdmin.setPhoneNumber("+1-555-0000");
                fallbackAdmin.setPermissions("ALL");
                fallbackAdmin.setSuperAdmin(true);
                model.addAttribute("systemAdministrator", fallbackAdmin);
            }
        }
        return "system-admin/profile";
    }

    /**
     * View specific booking details
     */
    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        List<Booking> bookings = systemAdministratorService.getAllBookings();
        Optional<Booking> booking = bookings.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
        
        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "system-admin/bookingDetails";
        } else {
            return "redirect:/system-admin/dashboard?error=Booking not found";
        }
    }

    /**
     * Edit customer user
     */
    @GetMapping("/users/customers/{id}/edit")
    public String editCustomer(@PathVariable Long id, Model model) {
        // For now, redirect to users page with edit mode
        return "redirect:/system-admin/users?edit=customer&id=" + id;
    }

    /**
     * Update customer user
     */
    @PostMapping("/users/customers/{id}/update")
    public String updateCustomer(@PathVariable Long id, 
                                @RequestParam String email,
                                @RequestParam boolean enabled,
                                Model model) {
        try {
            systemAdministratorService.updateCustomer(id, email, enabled);
            return "redirect:/system-admin/users?success=Customer updated successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update customer: " + e.getMessage();
        }
    }

    /**
     * Toggle customer user status
     */
    @PostMapping("/users/customers/{id}/toggle")
    public String toggleCustomer(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.toggleCustomer(id);
            return "redirect:/system-admin/users?success=Customer status updated";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update customer status: " + e.getMessage();
        }
    }

    /**
     * Edit hotel owner
     */
    @GetMapping("/users/hotel-owners/{id}/edit")
    public String editHotelOwner(@PathVariable Long id, Model model) {
        return "redirect:/system-admin/users?edit=hotel-owner&id=" + id;
    }

    /**
     * Update hotel owner
     */
    @PostMapping("/users/hotel-owners/{id}/update")
    public String updateHotelOwner(@PathVariable Long id,
                                  @RequestParam String ownerName,
                                  @RequestParam String email,
                                  @RequestParam String hotelName,
                                  @RequestParam boolean enabled,
                                  Model model) {
        try {
            systemAdministratorService.updateHotelOwner(id, ownerName, email, hotelName, enabled);
            return "redirect:/system-admin/users?success=Hotel owner updated successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update hotel owner: " + e.getMessage();
        }
    }

    /**
     * Toggle hotel owner status
     */
    @PostMapping("/users/hotel-owners/{id}/toggle")
    public String toggleHotelOwner(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.toggleHotelOwner(id);
            return "redirect:/system-admin/users?success=Hotel owner status updated";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update hotel owner status: " + e.getMessage();
        }
    }

    /**
     * Edit event coordinator
     */
    @GetMapping("/users/event-coordinators/{id}/edit")
    public String editEventCoordinator(@PathVariable Long id, Model model) {
        return "redirect:/system-admin/users?edit=event-coordinator&id=" + id;
    }

    /**
     * Update event coordinator
     */
    @PostMapping("/users/event-coordinators/{id}/update")
    public String updateEventCoordinator(@PathVariable Long id,
                                       @RequestParam String coordinatorName,
                                       @RequestParam String email,
                                       @RequestParam String department,
                                       @RequestParam boolean enabled,
                                       Model model) {
        try {
            systemAdministratorService.updateEventCoordinator(id, coordinatorName, email, department, enabled);
            return "redirect:/system-admin/users?success=Event coordinator updated successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update event coordinator: " + e.getMessage();
        }
    }

    /**
     * Toggle event coordinator status
     */
    @PostMapping("/users/event-coordinators/{id}/toggle")
    public String toggleEventCoordinator(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.toggleEventCoordinator(id);
            return "redirect:/system-admin/users?success=Event coordinator status updated";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update event coordinator status: " + e.getMessage();
        }
    }

    /**
     * Edit system administrator
     */
    @GetMapping("/users/system-admins/{id}/edit")
    public String editSystemAdmin(@PathVariable Long id, Model model) {
        return "redirect:/system-admin/users?edit=system-admin&id=" + id;
    }

    /**
     * Update system administrator
     */
    @PostMapping("/users/system-admins/{id}/update")
    public String updateSystemAdmin(@PathVariable Long id,
                                   @RequestParam String administratorName,
                                   @RequestParam String email,
                                   @RequestParam String role,
                                   @RequestParam boolean enabled,
                                   Model model) {
        try {
            systemAdministratorService.updateSystemAdministrator(id, administratorName, email, role, enabled);
            return "redirect:/system-admin/users?success=System administrator updated successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update system administrator: " + e.getMessage();
        }
    }

    /**
     * Toggle system administrator status
     */
    @PostMapping("/users/system-admins/{id}/toggle")
    public String toggleSystemAdmin(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.toggleSystemAdministrator(id);
            return "redirect:/system-admin/users?success=System administrator status updated";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update system administrator status: " + e.getMessage();
        }
    }

    // DELETE Operations

    /**
     * Delete customer user
     */
    @PostMapping("/users/customers/{id}/delete")
    public String deleteCustomer(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.deleteCustomer(id);
            return "redirect:/system-admin/users?success=Customer deleted successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to delete customer: " + e.getMessage();
        }
    }

    /**
     * Delete hotel owner
     */
    @PostMapping("/users/hotel-owners/{id}/delete")
    public String deleteHotelOwner(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.deleteHotelOwner(id);
            return "redirect:/system-admin/users?success=Hotel owner deleted successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to delete hotel owner: " + e.getMessage();
        }
    }

    /**
     * Delete event coordinator
     */
    @PostMapping("/users/event-coordinators/{id}/delete")
    public String deleteEventCoordinator(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.deleteEventCoordinator(id);
            return "redirect:/system-admin/users?success=Event coordinator deleted successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to delete event coordinator: " + e.getMessage();
        }
    }

    /**
     * Delete system administrator
     */
    @PostMapping("/users/system-admins/{id}/delete")
    public String deleteSystemAdmin(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.deleteSystemAdministrator(id);
            return "redirect:/system-admin/users?success=System administrator deleted successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to delete system administrator: " + e.getMessage();
        }
    }

    // Catering Manager Management Operations

    /**
     * Edit catering manager
     */
    @GetMapping("/users/catering-managers/{id}/edit")
    public String editCateringManager(@PathVariable Long id, Model model) {
        return "redirect:/system-admin/users?edit=catering-manager&id=" + id;
    }

    /**
     * Update catering manager
     */
    @PostMapping("/users/catering-managers/{id}/update")
    public String updateCateringManager(@PathVariable Long id, 
                                      @RequestParam String managerName,
                                      @RequestParam String email,
                                      @RequestParam String department,
                                      @RequestParam boolean enabled,
                                      Model model) {
        try {
            systemAdministratorService.updateCateringManager(id, managerName, email, department, enabled);
            return "redirect:/system-admin/users?success=Catering manager updated successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update catering manager: " + e.getMessage();
        }
    }

    /**
     * Toggle catering manager status
     */
    @PostMapping("/users/catering-managers/{id}/toggle")
    public String toggleCateringManager(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.toggleCateringManager(id);
            return "redirect:/system-admin/users?success=Catering manager status updated";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to update catering manager status: " + e.getMessage();
        }
    }

    /**
     * Delete catering manager
     */
    @PostMapping("/users/catering-managers/{id}/delete")
    public String deleteCateringManager(@PathVariable Long id, Model model) {
        try {
            systemAdministratorService.deleteCateringManager(id);
            return "redirect:/system-admin/users?success=Catering manager deleted successfully";
        } catch (Exception e) {
            return "redirect:/system-admin/users?error=Failed to delete catering manager: " + e.getMessage();
        }
    }

    /**
     * Cleanup old bookings without catering options
     */
    @PostMapping("/cleanup-old-bookings")
    public String cleanupOldBookings(RedirectAttributes redirectAttributes) {
        try {
            int deletedCount = systemAdministratorService.cleanupOldBookings();
            redirectAttributes.addFlashAttribute("successMessage", 
                "Cleanup completed! Deleted " + deletedCount + " old bookings without catering options.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error during cleanup: " + e.getMessage());
        }
        return "redirect:/system-admin/database";
    }
}
