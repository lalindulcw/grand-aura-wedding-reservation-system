package org.example.grandaura.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "system_administrators")
public class SystemAdministrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Administrator name is required")
    @Size(max = 100, message = "Administrator name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String administratorName;

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

    @Size(max = 100, message = "Department cannot exceed 100 characters")
    @Column(length = 100)
    private String department;

    @Size(max = 200, message = "Role cannot exceed 200 characters")
    @Column(length = 200)
    private String role;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Column(length = 20)
    private String phoneNumber;

    @Size(max = 500, message = "Permissions cannot exceed 500 characters")
    @Column(length = 500)
    private String permissions;

    @Column(nullable = false)
    private boolean superAdmin = false;

    public SystemAdministrator() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAdministratorName() { return administratorName; }
    public void setAdministratorName(String administratorName) { this.administratorName = administratorName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    public boolean isSuperAdmin() { return superAdmin; }
    public void setSuperAdmin(boolean superAdmin) { this.superAdmin = superAdmin; }

    @Override
    public String toString() {
        return "SystemAdministrator{" +
                "id=" + id +
                ", administratorName='" + administratorName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", superAdmin=" + superAdmin +
                '}';
    }
}
