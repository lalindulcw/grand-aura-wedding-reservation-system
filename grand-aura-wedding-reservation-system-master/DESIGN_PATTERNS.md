# Design Patterns Implemented in Grand Aura

This document outlines the design patterns used in the Grand Aura Hotel Reservation System.

## 1. **Model-View-Controller (MVC) Pattern** ✅

**Implementation:** Throughout the entire application

**Components:**
- **Model:** Entity classes (`Booking.java`, `UserAccount.java`, `HotelOwner.java`, etc.)
- **View:** Thymeleaf templates (`index.html`, `createBooking.html`, `bookingSuccess.html`, etc.)
- **Controller:** Controller classes (`BookingController.java`, `BookingUiController.java`, `HotelOwnerController.java`, etc.)

**Example:**
```java
// Model: src/main/java/org/example/grandaura/entity/Booking.java
@Entity
@Table(name = "booking")
public class Booking { ... }

// Controller: src/main/java/org/example/grandaura/controller/BookingController.java
@Controller
@RequestMapping("/api/bookings")
public class BookingController { 
    private final BookingService bookingService;
    
    @PostMapping
    public String createBooking(@ModelAttribute Booking booking) { ... }
}

// View: src/main/resources/templates/createBooking.html
<form th:action="@{/api/bookings}" th:object="${booking}">
    <!-- Form fields -->
</form>
```

**Benefits:**
- Separation of concerns
- Easier testing and maintenance
- Flexibility to change UI without affecting business logic

---

## 2. **Repository Pattern** ✅

**Implementation:** All data access layers using Spring Data JPA

**Example:**
```java
// src/main/java/org/example/grandaura/repository/BookingRepository.java
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerEmail(String customerEmail);
    List<Booking> findByWeddingDateBetween(LocalDate startDate, LocalDate endDate);
}

// src/main/java/org/example/grandaura/repository/UserRepository.java
@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
}
```

**Files Implementing This Pattern:**
- `BookingRepository.java`
- `UserRepository.java`
- `HotelOwnerRepository.java`
- `EventCoordinatorRepository.java`
- `SystemAdministratorRepository.java`
- `CateringManagerRepository.java`
- `FrontDeskOfficerRepository.java`
- `MenuRepository.java`
- `MenuPlanRepository.java`

**Benefits:**
- Abstraction of data access logic
- Easier to switch database implementations
- Consistent data access interface
- Built-in CRUD operations

---

## 3. **Service Layer Pattern (Business Delegate)** ✅

**Implementation:** Service classes that encapsulate business logic

**Example:**
```java
// src/main/java/org/example/grandaura/service/BookingService.java
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    
    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    
    public Booking saveBooking(Booking booking) {
        // Business logic here
        return bookingRepository.save(booking);
    }
    
    public List<Booking> getBookingsForCustomer(String email) {
        return bookingRepository.findByCustomerEmail(email);
    }
}
```

**Files Implementing This Pattern:**
- `BookingService.java`
- `AuthService.java`
- `HotelOwnerAuthService.java`
- `EventCoordinatorAuthService.java`
- `SystemAdministratorAuthService.java`
- `CateringManagerService.java`
- `FrontDeskOfficerService.java`

**Benefits:**
- Business logic separated from controllers
- Reusable business methods
- Transaction management
- Easier unit testing

---

## 4. **Dependency Injection (DI) Pattern** ✅

**Implementation:** Using Spring's `@Autowired` annotation

**Example:**
```java
@Controller
public class BookingController {
    
    private final BookingService bookingService;
    private final MenuRepository menuRepository;
    
    @Autowired
    public BookingController(BookingService bookingService, 
                            MenuRepository menuRepository) {
        this.bookingService = bookingService;
        this.menuRepository = menuRepository;
    }
}
```

**Benefits:**
- Loose coupling between components
- Easier testing (can inject mocks)
- Better code organization
- Automatic object lifecycle management

---

## 5. **Strategy Pattern** ✅

**Implementation:** Multiple authentication providers for different user types

**Example:**
```java
// src/main/java/org/example/grandaura/config/SecurityConfig.java
@Bean
public AuthenticationManager authenticationManager(
        AuthService authService,
        HotelOwnerAuthService hotelOwnerAuthService,
        EventCoordinatorAuthService eventCoordinatorAuthService,
        SystemAdministratorAuthService systemAdministratorAuthService,
        CateringManagerAuthService cateringManagerAuthService,
        FrontDeskOfficerAuthService frontDeskOfficerAuthService,
        PasswordEncoder passwordEncoder) {
    
    // Different authentication strategies for different user types
    DaoAuthenticationProvider userProvider = new DaoAuthenticationProvider();
    userProvider.setUserDetailsService(authService);
    
    DaoAuthenticationProvider hotelOwnerProvider = new DaoAuthenticationProvider();
    hotelOwnerProvider.setUserDetailsService(hotelOwnerAuthService);
    
    // ... more providers
    
    return new ProviderManager(userProvider, hotelOwnerProvider, ...);
}
```

**Benefits:**
- Different authentication logic for each user type
- Easy to add new user types
- Encapsulated authentication strategies

---

## 6. **Factory Pattern** ✅

**Implementation:** Spring Bean creation and initialization

**Example:**
```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        // Factory method creating configured security filter chain
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(...)
            .build();
    }
}
```

**Benefits:**
- Centralized object creation
- Configuration in one place
- Easy to change implementations

---

## 7. **Template Method Pattern** ✅

**Implementation:** Spring Data JPA repositories extend `JpaRepository`

**Example:**
```java
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // JpaRepository provides template methods:
    // - save()
    // - findById()
    // - findAll()
    // - deleteById()
    // etc.
    
    // We only define custom queries:
    List<Booking> findByCustomerEmail(String customerEmail);
}
```

**Benefits:**
- Common CRUD operations provided automatically
- Only implement custom behavior
- Consistent API across all repositories

---

## 8. **Front Controller Pattern** ✅

**Implementation:** Spring's `DispatcherServlet` (implicit)

**How It Works:**
All HTTP requests go through Spring's DispatcherServlet, which routes them to appropriate controllers.

```
HTTP Request → DispatcherServlet → Controller → Service → Repository → Database
```

**Benefits:**
- Centralized request handling
- Consistent routing
- Security filters applied uniformly

---

## 9. **Observer Pattern** ✅

**Implementation:** Spring Events (implicit in Spring Security)

**Example:**
```java
// Custom authentication success handler observes login events
@Component
public class CustomAuthenticationSuccessHandler 
    implements AuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) {
        // React to successful authentication event
        Collection<? extends GrantedAuthority> authorities = 
            authentication.getAuthorities();
        
        // Route based on user role
        if (hasRole(authorities, "ROLE_HOTEL_OWNER")) {
            response.sendRedirect("/hotel-owner/dashboard");
        } else if (hasRole(authorities, "ROLE_CATERING_MANAGER")) {
            response.sendRedirect("/catering-manager/dashboard");
        }
        // ...
    }
}
```

**Benefits:**
- Decoupled event handling
- Multiple observers can react to same event
- Easy to add new event handlers

---

## 10. **Builder Pattern** ✅

**Implementation:** Entity construction and HTTP security configuration

**Example:**
```java
// HTTP Security Builder
http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/login").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
    )
    .formLogin(login -> login
        .loginPage("/login")
        .successHandler(customAuthenticationSuccessHandler)
    )
    .build();
```

**Benefits:**
- Fluent, readable configuration
- Step-by-step object construction
- Complex objects built easily

---

## Summary of Design Patterns

| Pattern | Location | Purpose |
|---------|----------|---------|
| **MVC** | Entire application | Separates presentation, business logic, and data |
| **Repository** | `repository/` package | Data access abstraction |
| **Service Layer** | `service/` package | Business logic encapsulation |
| **Dependency Injection** | Throughout | Loose coupling, testability |
| **Strategy** | `SecurityConfig.java` | Multiple authentication strategies |
| **Factory** | `@Configuration` classes | Bean creation |
| **Template Method** | Spring Data JPA | Common CRUD operations |
| **Front Controller** | Spring DispatcherServlet | Centralized request routing |
| **Observer** | Authentication handlers | Event-driven architecture |
| **Builder** | Security config | Fluent object construction |

---

## Key Takeaway for Requirements

✅ **Your application implements MULTIPLE professional design patterns**, far exceeding the "minimum 1 design pattern" requirement!

The most prominent patterns are:
1. **MVC Pattern** - Core architectural pattern
2. **Repository Pattern** - All data access
3. **Service Layer Pattern** - Business logic layer
4. **Dependency Injection** - Throughout the application
5. **Strategy Pattern** - Authentication mechanisms

You can confidently document any of these patterns in your project report or presentation.

