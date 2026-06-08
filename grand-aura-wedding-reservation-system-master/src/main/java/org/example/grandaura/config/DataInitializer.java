package org.example.grandaura.config;

import org.example.grandaura.entity.HotelOwner;
import org.example.grandaura.entity.EventCoordinator;
import org.example.grandaura.entity.SystemAdministrator;
import org.example.grandaura.entity.CateringManager;
import org.example.grandaura.entity.FrontDeskOfficer;
import org.example.grandaura.entity.Menu;
import org.example.grandaura.repository.HotelOwnerRepository;
import org.example.grandaura.repository.EventCoordinatorRepository;
import org.example.grandaura.repository.SystemAdministratorRepository;
import org.example.grandaura.repository.CateringManagerRepository;
import org.example.grandaura.repository.FrontDeskOfficerRepository;
import org.example.grandaura.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data initializer to create test Hotel Owner account
 * This runs when the application starts and creates a default hotel owner
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final HotelOwnerRepository hotelOwnerRepository;
    private final EventCoordinatorRepository eventCoordinatorRepository;
    private final SystemAdministratorRepository systemAdministratorRepository;
    private final CateringManagerRepository cateringManagerRepository;
    private final FrontDeskOfficerRepository frontDeskOfficerRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(HotelOwnerRepository hotelOwnerRepository, 
                          EventCoordinatorRepository eventCoordinatorRepository,
                          SystemAdministratorRepository systemAdministratorRepository,
                          CateringManagerRepository cateringManagerRepository,
                          FrontDeskOfficerRepository frontDeskOfficerRepository,
                          MenuRepository menuRepository,
                          PasswordEncoder passwordEncoder) {
        this.hotelOwnerRepository = hotelOwnerRepository;
        this.eventCoordinatorRepository = eventCoordinatorRepository;
        this.systemAdministratorRepository = systemAdministratorRepository;
        this.cateringManagerRepository = cateringManagerRepository;
        this.frontDeskOfficerRepository = frontDeskOfficerRepository;
        this.menuRepository = menuRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if hotel owner already exists
        if (hotelOwnerRepository.count() == 0) {
            createTestHotelOwner();
        }
        
        // Check if event coordinator already exists
        if (eventCoordinatorRepository.count() == 0) {
            createTestEventCoordinator();
        }
        
        // Check if system administrator already exists
        if (systemAdministratorRepository.count() == 0) {
            createTestSystemAdministrator();
        }
        
        // Check if catering manager already exists
        if (cateringManagerRepository.count() == 0) {
            createTestCateringManager();
        }
        
        // Check if front desk officer already exists
        if (frontDeskOfficerRepository.count() == 0) {
            createTestFrontDeskOfficer();
        }
        
        // Check if menu items already exist
        if (menuRepository.count() == 0) {
            createSampleMenuItems();
        }
    }

    private void createTestHotelOwner() {
        HotelOwner hotelOwner = new HotelOwner();
        hotelOwner.setOwnerName("John Smith");
        hotelOwner.setEmail("hotel.owner@grandaura.com");
        hotelOwner.setPasswordHash(passwordEncoder.encode("hotel123"));
        hotelOwner.setHotelName("Grand Aura Hotel");
        hotelOwner.setDescription("Luxury wedding venue specializing in elegant celebrations");
        hotelOwner.setEnabled(true);

        hotelOwnerRepository.save(hotelOwner);
        
        System.out.println("✅ Test Hotel Owner account created:");
        System.out.println("📧 Email: hotel.owner@grandaura.com");
        System.out.println("🔑 Password: hotel123");
        System.out.println("🏨 Hotel: Grand Aura Hotel");
        System.out.println("🌐 Login URL: http://localhost:8080/hotel-owner/login");
    }

    private void createTestEventCoordinator() {
        EventCoordinator eventCoordinator = new EventCoordinator();
        eventCoordinator.setCoordinatorName("Sarah Johnson");
        eventCoordinator.setEmail("coordinator@grandaura.com");
        eventCoordinator.setPasswordHash(passwordEncoder.encode("coordinator123"));
        eventCoordinator.setDepartment("Wedding Planning");
        eventCoordinator.setSpecialization("Luxury Wedding Coordination");
        eventCoordinator.setPhoneNumber("+1-555-0123");
        eventCoordinator.setEnabled(true);

        eventCoordinatorRepository.save(eventCoordinator);
        
        System.out.println("✅ Test Event Coordinator account created:");
        System.out.println("📧 Email: coordinator@grandaura.com");
        System.out.println("🔑 Password: coordinator123");
        System.out.println("🎭 Role: Event Coordinator");
        System.out.println("🌐 Login URL: http://localhost:8080/event-coordinator/login");
    }

    private void createTestSystemAdministrator() {
        SystemAdministrator systemAdmin = new SystemAdministrator();
        systemAdmin.setAdministratorName("Admin User");
        systemAdmin.setEmail("admin@grandaura.com");
        systemAdmin.setPasswordHash(passwordEncoder.encode("admin123"));
        systemAdmin.setDepartment("IT Administration");
        systemAdmin.setRole("System Administrator");
        systemAdmin.setPhoneNumber("+1-555-0000");
        systemAdmin.setPermissions("ALL");
        systemAdmin.setSuperAdmin(true);
        systemAdmin.setEnabled(true);

        systemAdministratorRepository.save(systemAdmin);
        
        System.out.println("✅ Test System Administrator account created:");
        System.out.println("📧 Email: admin@grandaura.com");
        System.out.println("🔑 Password: admin123");
        System.out.println("🖥️ Role: System Administrator");
        System.out.println("🌐 Login URL: http://localhost:8080/system-admin/login");
    }

    private void createTestCateringManager() {
        CateringManager cateringManager = new CateringManager();
        cateringManager.setManagerName("Michael Chen");
        cateringManager.setEmail("catering@grandaura.com");
        cateringManager.setPasswordHash(passwordEncoder.encode("catering123"));
        cateringManager.setDepartment("Catering Services");
        cateringManager.setSpecialization("Wedding Catering");
        cateringManager.setPhoneNumber("+1-555-0124");
        cateringManager.setExperienceLevel("Senior");
        cateringManager.setCertifications("Certified Wedding Planner, Food Safety Certified");
        cateringManager.setEnabled(true);

        cateringManagerRepository.save(cateringManager);
        
        System.out.println("✅ Test Catering Manager account created:");
        System.out.println("📧 Email: catering@grandaura.com");
        System.out.println("🔑 Password: catering123");
        System.out.println("🍽️ Role: Catering Manager");
        System.out.println("🌐 Login URL: http://localhost:8080/catering-manager/login");
    }

    private void createTestFrontDeskOfficer() {
        FrontDeskOfficer officer = new FrontDeskOfficer();
        officer.setOfficerName("Sarah Johnson");
        officer.setEmail("frontdesk@grandaura.com");
        officer.setPasswordHash(passwordEncoder.encode("frontdesk123"));
        officer.setShift("Morning");
        officer.setDepartment("Reception");
        officer.setPhoneNumber("+1-555-0125");
        officer.setEmployeeId("FD001");
        officer.setLanguages("English, Spanish, French");
        officer.setAccessLevel("Senior");
        officer.setEnabled(true);

        frontDeskOfficerRepository.save(officer);
        
        System.out.println("✅ Test Front Desk Officer account created:");
        System.out.println("📧 Email: frontdesk@grandaura.com");
        System.out.println("🔑 Password: frontdesk123");
        System.out.println("👔 Role: Front Desk Officer");
        System.out.println("🌐 Login URL: http://localhost:8080/front-desk/login");
    }

    private void createSampleMenuItems() {
        // Appetizers
        Menu appetizer1 = new Menu();
        appetizer1.setName("Tandoori Chicken Tikka");
        appetizer1.setDescription("Succulent chicken pieces marinated in yogurt and spices, cooked in clay oven");
        appetizer1.setPrice(new java.math.BigDecimal("18.99"));
        appetizer1.setCategory("Appetizer");
        appetizer1.setCuisine("Indian");
        appetizer1.setDietaryInfo("Gluten-Free");
        appetizer1.setPreparationTime(25);
        appetizer1.setAllergens("Contains dairy");
        appetizer1.setIsAvailable(true);
        menuRepository.save(appetizer1);

        Menu appetizer2 = new Menu();
        appetizer2.setName("Bruschetta Trio");
        appetizer2.setDescription("Three varieties of bruschetta: tomato basil, mushroom, and roasted pepper");
        appetizer2.setPrice(new java.math.BigDecimal("16.99"));
        appetizer2.setCategory("Appetizer");
        appetizer2.setCuisine("Italian");
        appetizer2.setDietaryInfo("Vegetarian");
        appetizer2.setPreparationTime(15);
        appetizer2.setAllergens("Contains gluten");
        appetizer2.setIsAvailable(true);
        menuRepository.save(appetizer2);

        // Main Courses
        Menu main1 = new Menu();
        main1.setName("Grilled Salmon with Lemon Herb Sauce");
        main1.setDescription("Fresh Atlantic salmon grilled to perfection, served with lemon herb sauce and seasonal vegetables");
        main1.setPrice(new java.math.BigDecimal("32.99"));
        main1.setCategory("Main Course");
        main1.setCuisine("Continental");
        main1.setDietaryInfo("Gluten-Free");
        main1.setPreparationTime(30);
        main1.setAllergens("Contains fish");
        main1.setIsAvailable(true);
        menuRepository.save(main1);

        Menu main2 = new Menu();
        main2.setName("Chicken Biryani");
        main2.setDescription("Fragrant basmati rice cooked with tender chicken, aromatic spices, and saffron");
        main2.setPrice(new java.math.BigDecimal("28.99"));
        main2.setCategory("Main Course");
        main2.setCuisine("Indian");
        main2.setDietaryInfo("Gluten-Free");
        main2.setPreparationTime(45);
        main2.setAllergens("Contains dairy");
        main2.setIsAvailable(true);
        menuRepository.save(main2);

        Menu main3 = new Menu();
        main3.setName("Vegetarian Pasta Primavera");
        main3.setDescription("Fresh vegetables and herbs tossed with al dente pasta in olive oil and garlic sauce");
        main3.setPrice(new java.math.BigDecimal("24.99"));
        main3.setCategory("Main Course");
        main3.setCuisine("Italian");
        main3.setDietaryInfo("Vegetarian");
        main3.setPreparationTime(20);
        main3.setAllergens("Contains gluten, dairy");
        main3.setIsAvailable(true);
        menuRepository.save(main3);

        // Desserts
        Menu dessert1 = new Menu();
        dessert1.setName("Chocolate Lava Cake");
        dessert1.setDescription("Warm chocolate cake with molten center, served with vanilla ice cream");
        dessert1.setPrice(new java.math.BigDecimal("12.99"));
        dessert1.setCategory("Dessert");
        dessert1.setCuisine("Continental");
        dessert1.setDietaryInfo("Vegetarian");
        dessert1.setPreparationTime(15);
        dessert1.setAllergens("Contains gluten, dairy, eggs");
        dessert1.setIsAvailable(true);
        menuRepository.save(dessert1);

        Menu dessert2 = new Menu();
        dessert2.setName("Gulab Jamun");
        dessert2.setDescription("Soft milk dumplings in rose-flavored syrup, served warm");
        dessert2.setPrice(new java.math.BigDecimal("10.99"));
        dessert2.setCategory("Dessert");
        dessert2.setCuisine("Indian");
        dessert2.setDietaryInfo("Vegetarian");
        dessert2.setPreparationTime(10);
        dessert2.setAllergens("Contains dairy");
        dessert2.setIsAvailable(true);
        menuRepository.save(dessert2);

        // Beverages
        Menu beverage1 = new Menu();
        beverage1.setName("Fresh Mango Lassi");
        beverage1.setDescription("Creamy yogurt drink blended with fresh mango and cardamom");
        beverage1.setPrice(new java.math.BigDecimal("6.99"));
        beverage1.setCategory("Beverage");
        beverage1.setCuisine("Indian");
        beverage1.setDietaryInfo("Vegetarian");
        beverage1.setPreparationTime(5);
        beverage1.setAllergens("Contains dairy");
        beverage1.setIsAvailable(true);
        menuRepository.save(beverage1);

        Menu beverage2 = new Menu();
        beverage2.setName("Sparkling Lemonade");
        beverage2.setDescription("Fresh lemon juice with sparkling water and mint");
        beverage2.setPrice(new java.math.BigDecimal("5.99"));
        beverage2.setCategory("Beverage");
        beverage2.setCuisine("Continental");
        beverage2.setDietaryInfo("Vegan");
        beverage2.setPreparationTime(3);
        beverage2.setAllergens("None");
        beverage2.setIsAvailable(true);
        menuRepository.save(beverage2);

        System.out.println("✅ Sample menu items created successfully!");
        System.out.println("🍽️ Total menu items: " + menuRepository.count());
    }
}
