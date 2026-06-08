package org.example.grandaura.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Event Coordinator entity for managing wedding events and coordination
 * This entity represents event coordinators who handle wedding planning,
 * vendor coordination, and event execution.
 */
@Entity
@Table(name = "event_coordinators")
public class EventCoordinator {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coordinator name is required")
    @Size(max = 100, message = "Coordinator name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String coordinatorName;

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

    // Default constructor (required by JPA)
    public EventCoordinator() {}

    // Constructor for creating new event coordinator
    public EventCoordinator(String coordinatorName, String email, String passwordHash, String department) {
        this.coordinatorName = coordinatorName;
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

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
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

    @Override
    public String toString() {
        return "EventCoordinator{" +
                "id=" + id +
                ", coordinatorName='" + coordinatorName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

