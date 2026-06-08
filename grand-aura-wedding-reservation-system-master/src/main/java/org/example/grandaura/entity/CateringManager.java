package org.example.grandaura.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Catering Manager entity for managing wedding catering services
 * This entity represents catering managers who handle menu planning,
 * food service coordination, and vendor management for wedding events.
 */
@Entity
@Table(name = "catering_managers")
public class CateringManager {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Manager name is required")
    @Size(max = 100, message = "Manager name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String managerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 160, message = "Email cannot exceed 160 characters")
    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @NotBlank(message = "Password hash is required")
    @Size(min = 8)
    @Column(nullable = false, length = 200)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = true;

    @Size(max = 200, message = "Department cannot exceed 200 characters")
    @Column(length = 200)
    private String department;

    @Size(max = 500, message = "Specialization cannot exceed 500 characters")
    @Column(length = 500)
    private String specialization;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    @Column(length = 15)
    private String phoneNumber;

    @Size(max = 100, message = "Experience level cannot exceed 100 characters")
    @Column(length = 100)
    private String experienceLevel;

    @Size(max = 500, message = "Certifications cannot exceed 500 characters")
    @Column(length = 500)
    private String certifications;

    // Default constructor (required by JPA)
    public CateringManager() {}

    // Constructor for creating new catering manager
    public CateringManager(String managerName, String email, String passwordHash, String department) {
        this.managerName = managerName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.department = department;
        this.enabled = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }
}

