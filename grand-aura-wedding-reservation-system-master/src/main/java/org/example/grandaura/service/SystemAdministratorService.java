package org.example.grandaura.service;

import org.example.grandaura.entity.Booking;
import org.example.grandaura.entity.EventCoordinator;
import org.example.grandaura.entity.HotelOwner;
import org.example.grandaura.entity.SystemAdministrator;
import org.example.grandaura.entity.UserAccount;
import org.example.grandaura.entity.CateringManager;
import org.example.grandaura.entity.FrontDeskOfficer;
import org.example.grandaura.repository.BookingRepository;
import org.example.grandaura.repository.EventCoordinatorRepository;
import org.example.grandaura.repository.HotelOwnerRepository;
import org.example.grandaura.repository.SystemAdministratorRepository;
import org.example.grandaura.repository.UserAccountRepository;
import org.example.grandaura.repository.CateringManagerRepository;
import org.example.grandaura.repository.FrontDeskOfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for System Administrator operations
 * Provides system-wide management, user administration, and analytics
 */
@Service
public class SystemAdministratorService {

    private final SystemAdministratorRepository systemAdministratorRepository;
    private final UserAccountRepository userAccountRepository;
    private final HotelOwnerRepository hotelOwnerRepository;
    private final EventCoordinatorRepository eventCoordinatorRepository;
    private final CateringManagerRepository cateringManagerRepository;
    private final FrontDeskOfficerRepository frontDeskOfficerRepository;
    private final BookingRepository bookingRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public SystemAdministratorService(SystemAdministratorRepository systemAdministratorRepository,
                                      UserAccountRepository userAccountRepository,
                                      HotelOwnerRepository hotelOwnerRepository,
                                      EventCoordinatorRepository eventCoordinatorRepository,
                                      CateringManagerRepository cateringManagerRepository,
                                      FrontDeskOfficerRepository frontDeskOfficerRepository,
                                      BookingRepository bookingRepository) {
        this.systemAdministratorRepository = systemAdministratorRepository;
        this.userAccountRepository = userAccountRepository;
        this.hotelOwnerRepository = hotelOwnerRepository;
        this.eventCoordinatorRepository = eventCoordinatorRepository;
        this.cateringManagerRepository = cateringManagerRepository;
        this.frontDeskOfficerRepository = frontDeskOfficerRepository;
        this.bookingRepository = bookingRepository;
    }

    // System Administrator Management Methods

    /**
     * Save or update system administrator
     */
    public SystemAdministrator saveSystemAdministrator(SystemAdministrator systemAdministrator) {
        return systemAdministratorRepository.save(systemAdministrator);
    }

    /**
     * Find system administrator by email
     */
    public Optional<SystemAdministrator> findByEmail(String email) {
        return systemAdministratorRepository.findByEmail(email);
    }

    /**
     * Get all system administrators
     */
    public List<SystemAdministrator> getAllSystemAdministrators() {
        return systemAdministratorRepository.findAll();
    }


    // User Management Methods

    /**
     * Get all user accounts
     */
    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.findAll();
    }

    /**
     * Get all hotel owners
     */
    public List<HotelOwner> getAllHotelOwners() {
        return hotelOwnerRepository.findAll();
    }

    /**
     * Get all event coordinators
     */
    public List<EventCoordinator> getAllEventCoordinators() {
        return eventCoordinatorRepository.findAll();
    }

    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get total user count by type
     */
    public Map<String, Long> getUserCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("customers", userAccountRepository.count());
        counts.put("hotelOwners", hotelOwnerRepository.count());
        counts.put("eventCoordinators", eventCoordinatorRepository.count());
        counts.put("systemAdministrators", systemAdministratorRepository.count());
        return counts;
    }

    /**
     * Get system statistics
     */
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // User statistics
        stats.put("totalUsers", getUserCounts().values().stream().mapToLong(Long::longValue).sum());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("totalGuests", bookingRepository.findAll().stream().mapToInt(Booking::getGuestCount).sum());
        
        // Recent activity
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        long recentBookings = bookingRepository.findAll().stream()
                .filter(booking -> !booking.getWeddingDate().isBefore(thirtyDaysAgo))
                .count();
        stats.put("recentBookings", recentBookings);
        
        // Venue utilization
        Map<String, Long> venueUtilization = bookingRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Booking::getVenue,
                        java.util.stream.Collectors.counting()
                ));
        stats.put("venueUtilization", venueUtilization);
        
        // Monthly trends
        Map<String, Long> monthlyStats = bookingRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        booking -> booking.getWeddingDate().getMonth().toString(),
                        java.util.stream.Collectors.counting()
                ));
        stats.put("monthlyStats", monthlyStats);
        
        return stats;
    }

    /**
     * Get database health metrics
     */
    public Map<String, Object> getDatabaseHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // Test database connectivity
            long userCount = userAccountRepository.count();
            long bookingCount = bookingRepository.count();
            
            health.put("status", "healthy");
            health.put("userAccounts", userCount);
            health.put("bookings", bookingCount);
            health.put("lastChecked", java.time.LocalDateTime.now());
            
        } catch (Exception e) {
            health.put("status", "error");
            health.put("error", e.getMessage());
            health.put("lastChecked", java.time.LocalDateTime.now());
        }
        
        return health;
    }

    /**
     * Get system performance metrics
     */
    public Map<String, Object> getSystemPerformance() {
        Map<String, Object> performance = new HashMap<>();
        
        // Calculate average booking value (simplified)
        List<Booking> bookings = bookingRepository.findAll();
        double averageGuestCount = bookings.stream()
                .mapToInt(Booking::getGuestCount)
                .average()
                .orElse(0.0);
        
        performance.put("averageGuestCount", averageGuestCount);
        performance.put("totalBookings", bookings.size());
        performance.put("systemUptime", "99.9%"); // Placeholder
        performance.put("responseTime", "150ms"); // Placeholder
        
        return performance;
    }

    /**
     * Get user activity summary
     */
    public Map<String, Object> getUserActivity() {
        Map<String, Object> activity = new HashMap<>();
        
        // Recent user registrations (last 30 days)
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        long recentRegistrations = userAccountRepository.findAll().stream()
                .filter(user -> user.getEmail().contains("@")) // Simplified check
                .count();
        
        activity.put("recentRegistrations", recentRegistrations);
        activity.put("activeUsers", getUserCounts().get("customers"));
        activity.put("totalSessions", "1,234"); // Placeholder
        activity.put("averageSessionTime", "25 minutes"); // Placeholder
        
        return activity;
    }

    // User Management Methods

    /**
     * Update customer user
     */
    public void updateCustomer(Long id, String email, boolean enabled) {
        Optional<UserAccount> customerOpt = userAccountRepository.findById(id);
        if (customerOpt.isPresent()) {
            UserAccount customer = customerOpt.get();
            customer.setEmail(email);
            customer.setEnabled(enabled);
            userAccountRepository.save(customer);
        }
    }

    /**
     * Toggle customer user status
     */
    public void toggleCustomer(Long id) {
        Optional<UserAccount> customerOpt = userAccountRepository.findById(id);
        if (customerOpt.isPresent()) {
            UserAccount customer = customerOpt.get();
            customer.setEnabled(!customer.isEnabled());
            userAccountRepository.save(customer);
        }
    }

    /**
     * Update hotel owner
     */
    public void updateHotelOwner(Long id, String ownerName, String email, String hotelName, boolean enabled) {
        Optional<HotelOwner> ownerOpt = hotelOwnerRepository.findById(id);
        if (ownerOpt.isPresent()) {
            HotelOwner owner = ownerOpt.get();
            owner.setOwnerName(ownerName);
            owner.setEmail(email);
            owner.setHotelName(hotelName);
            owner.setEnabled(enabled);
            hotelOwnerRepository.save(owner);
        }
    }

    /**
     * Toggle hotel owner status
     */
    public void toggleHotelOwner(Long id) {
        Optional<HotelOwner> ownerOpt = hotelOwnerRepository.findById(id);
        if (ownerOpt.isPresent()) {
            HotelOwner owner = ownerOpt.get();
            owner.setEnabled(!owner.isEnabled());
            hotelOwnerRepository.save(owner);
        }
    }

    /**
     * Update event coordinator
     */
    public void updateEventCoordinator(Long id, String coordinatorName, String email, String department, boolean enabled) {
        Optional<EventCoordinator> coordinatorOpt = eventCoordinatorRepository.findById(id);
        if (coordinatorOpt.isPresent()) {
            EventCoordinator coordinator = coordinatorOpt.get();
            coordinator.setCoordinatorName(coordinatorName);
            coordinator.setEmail(email);
            coordinator.setDepartment(department);
            coordinator.setEnabled(enabled);
            eventCoordinatorRepository.save(coordinator);
        }
    }

    /**
     * Toggle event coordinator status
     */
    public void toggleEventCoordinator(Long id) {
        Optional<EventCoordinator> coordinatorOpt = eventCoordinatorRepository.findById(id);
        if (coordinatorOpt.isPresent()) {
            EventCoordinator coordinator = coordinatorOpt.get();
            coordinator.setEnabled(!coordinator.isEnabled());
            eventCoordinatorRepository.save(coordinator);
        }
    }

    /**
     * Update system administrator
     */
    public void updateSystemAdministrator(Long id, String administratorName, String email, String role, boolean enabled) {
        Optional<SystemAdministrator> adminOpt = systemAdministratorRepository.findById(id);
        if (adminOpt.isPresent()) {
            SystemAdministrator admin = adminOpt.get();
            admin.setAdministratorName(administratorName);
            admin.setEmail(email);
            admin.setRole(role);
            admin.setEnabled(enabled);
            systemAdministratorRepository.save(admin);
        }
    }

    /**
     * Toggle system administrator status
     */
    public void toggleSystemAdministrator(Long id) {
        Optional<SystemAdministrator> adminOpt = systemAdministratorRepository.findById(id);
        if (adminOpt.isPresent()) {
            SystemAdministrator admin = adminOpt.get();
            admin.setEnabled(!admin.isEnabled());
            systemAdministratorRepository.save(admin);
        }
    }

    // DELETE Operations

    /**
     * Delete customer user
     */
    public void deleteCustomer(Long id) {
        userAccountRepository.deleteById(id);
    }

    /**
     * Delete hotel owner
     */
    public void deleteHotelOwner(Long id) {
        hotelOwnerRepository.deleteById(id);
    }

    /**
     * Delete event coordinator
     */
    public void deleteEventCoordinator(Long id) {
        eventCoordinatorRepository.deleteById(id);
    }

    /**
     * Delete system administrator
     */
    public void deleteSystemAdministrator(Long id) {
        systemAdministratorRepository.deleteById(id);
    }

    // Catering Manager Management Methods

    /**
     * Get all catering managers
     */
    public List<CateringManager> getAllCateringManagers() {
        return cateringManagerRepository.findAll();
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

    /**
     * Delete catering manager
     */
    public void deleteCateringManager(Long id) {
        cateringManagerRepository.deleteById(id);
    }

    /**
     * Cleanup old bookings without catering options
     */
    public int cleanupOldBookings() {
        List<Booking> oldBookings = bookingRepository.findAll().stream()
            .filter(booking -> 
                (booking.getBrideName() == null || booking.getBrideName().isEmpty()) &&
                (booking.getGroomName() == null || booking.getGroomName().isEmpty()) &&
                (booking.getPreferredVenue() == null || booking.getPreferredVenue().isEmpty()) &&
                (booking.getCateringPackage() == null || booking.getCateringPackage().isEmpty()) &&
                (booking.getDietaryRequirements() == null || booking.getDietaryRequirements().isEmpty()) &&
                (booking.getSpecialCateringRequests() == null || booking.getSpecialCateringRequests().isEmpty()) &&
                (booking.getEstimatedGuestCount() == null || booking.getEstimatedGuestCount() == 0)
            )
            .collect(java.util.stream.Collectors.toList());
        
        int count = oldBookings.size();
        bookingRepository.deleteAll(oldBookings);
        return count;
    }

    /**
     * Create new user based on type
     */
    public void createUser(String userType, String email, String password,
                          String ownerName, String hotelName, String description,
                          String coordinatorName, String phoneNumber, String specialization,
                          String adminName, String department,
                          String managerName, String cateringDepartment,
                          String officerName, String shift, String fdDepartment, String employeeId) {
        
        String hashedPassword = passwordEncoder.encode(password);
        
        switch (userType) {
            case "CUSTOMER":
                UserAccount customer = new UserAccount();
                customer.setEmail(email);
                customer.setPasswordHash(hashedPassword);
                customer.setEnabled(true);
                userAccountRepository.save(customer);
                break;
                
            case "HOTEL_OWNER":
                HotelOwner hotelOwner = new HotelOwner();
                hotelOwner.setEmail(email);
                hotelOwner.setPasswordHash(hashedPassword);
                hotelOwner.setOwnerName(ownerName != null ? ownerName : "Hotel Owner");
                hotelOwner.setHotelName(hotelName != null ? hotelName : "Grand Aura");
                hotelOwner.setDescription(description);
                hotelOwner.setEnabled(true);
                hotelOwnerRepository.save(hotelOwner);
                break;
                
            case "EVENT_COORDINATOR":
                EventCoordinator coordinator = new EventCoordinator();
                coordinator.setEmail(email);
                coordinator.setPasswordHash(hashedPassword);
                coordinator.setCoordinatorName(coordinatorName != null ? coordinatorName : "Event Coordinator");
                coordinator.setPhoneNumber(phoneNumber);
                coordinator.setSpecialization(specialization);
                coordinator.setEnabled(true);
                eventCoordinatorRepository.save(coordinator);
                break;
                
            case "SYSTEM_ADMIN":
                SystemAdministrator admin = new SystemAdministrator();
                admin.setEmail(email);
                admin.setPasswordHash(hashedPassword);
                admin.setAdministratorName(adminName != null ? adminName : "System Admin");
                admin.setDepartment(department != null ? department : "IT Department");
                admin.setEnabled(true);
                systemAdministratorRepository.save(admin);
                break;
                
            case "CATERING_MANAGER":
                CateringManager manager = new CateringManager();
                manager.setEmail(email);
                manager.setPasswordHash(hashedPassword);
                manager.setManagerName(managerName != null ? managerName : "Catering Manager");
                manager.setDepartment(cateringDepartment != null ? cateringDepartment : "Catering & Events");
                manager.setEnabled(true);
                cateringManagerRepository.save(manager);
                break;
                
            case "FRONT_DESK":
                FrontDeskOfficer officer = new FrontDeskOfficer();
                officer.setEmail(email);
                officer.setPasswordHash(hashedPassword);
                officer.setOfficerName(officerName != null ? officerName : "Front Desk Officer");
                officer.setShift(shift);
                officer.setDepartment(fdDepartment != null ? fdDepartment : "Front Desk Operations");
                officer.setEmployeeId(employeeId);
                officer.setEnabled(true);
                frontDeskOfficerRepository.save(officer);
                break;
                
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}
