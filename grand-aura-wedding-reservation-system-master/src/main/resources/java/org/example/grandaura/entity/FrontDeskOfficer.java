package org.example.grandaura.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a Front Desk Officer in the system
 * Handles guest check-in/out, booking confirmations, and customer service
 */
@Entity
@Table(name = "front_desk_officers")
public class FrontDeskOfficer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Officer name is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String officerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Password hash is required")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Size(max = 50)
    @Column(length = 50)
    private String shift; // e.g., Morning, Afternoon, Evening, Night

    @Size(max = 50)
    @Column(length = 50)
    private String department; // e.g., Reception, Concierge, Guest Services

    @Size(max = 20)
    @Column(length = 20)
    private String phoneNumber;

    @Size(max = 50)
    @Column(length = 50)
    private String employeeId;

    @Size(max = 100)
    @Column(length = 100)
    private String languages; // Languages spoken (e.g., English, Spanish, French)

    @NotBlank(message = "Access level is required")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String accessLevel; // e.g., Junior, Senior, Supervisor

    @Column(nullable = false)
    private Boolean enabled = true;

    // Default constructor
    public FrontDeskOfficer() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOfficerName() { return officerName; }
    public void setOfficerName(String officerName) { this.officerName = officerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
