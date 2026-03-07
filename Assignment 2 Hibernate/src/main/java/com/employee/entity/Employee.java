package com.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entity class mapped to the 'employee' table.
 * Has a One-to-One relationship with Address.
 * 
 * Validations:
 *   - Email must be unique
 *   - Salary must be greater than 0
 *   - Phone number must contain exactly 10 digits
 */
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_name", nullable = false)
    @NotBlank(message = "Employee name cannot be blank")
    private String employeeName;

    @Column(name = "email", unique = true, nullable = false)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(name = "phone", nullable = false)
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must contain exactly 10 digits")
    private String phone;

    @Column(name = "salary", nullable = false)
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0")
    private double salary;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * One-to-One mapping with Address.
     * CascadeType.ALL: All operations (persist, merge, remove) cascade to Address.
     * orphanRemoval: When employee is deleted, address is also deleted.
     */
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true,
              fetch = FetchType.EAGER)
    private Address address;

    // ========================
    // Constructors
    // ========================

    public Employee() {
    }

    public Employee(String employeeName, String email, String gender,
                    String password, String phone, double salary, LocalDate dateOfBirth) {
        this.employeeName = employeeName;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.phone = phone;
        this.salary = salary;
        this.dateOfBirth = dateOfBirth;
    }

    // ========================
    // Getters and Setters
    // ========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            address.setEmployee(this);
        }
    }

    // ========================
    // toString
    // ========================

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-20s | %-25s | %-8s | %-12s | %10.2f | %-12s |",
            id, employeeName, email, gender, phone, salary, dateOfBirth
        );
    }

    public String toDetailString() {
        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("  EMPLOYEE DETAILS\n");
        sb.append("============================================\n");
        sb.append("  ID             : ").append(id).append("\n");
        sb.append("  Name           : ").append(employeeName).append("\n");
        sb.append("  Email          : ").append(email).append("\n");
        sb.append("  Gender         : ").append(gender).append("\n");
        sb.append("  Phone          : ").append(phone).append("\n");
        sb.append("  Salary         : ").append(String.format("%.2f", salary)).append("\n");
        sb.append("  Date of Birth  : ").append(dateOfBirth).append("\n");

        if (address != null) {
            sb.append("  ---------- ADDRESS ----------\n");
            sb.append("  Address ID     : ").append(address.getId()).append("\n");
            sb.append("  Street         : ").append(address.getStreet()).append("\n");
            sb.append("  City           : ").append(address.getCity()).append("\n");
            sb.append("  State          : ").append(address.getState()).append("\n");
            sb.append("  Country        : ").append(address.getCountry()).append("\n");
            sb.append("  Pincode        : ").append(address.getPincode()).append("\n");
        } else {
            sb.append("  Address        : Not assigned\n");
        }

        sb.append("============================================");
        return sb.toString();
    }
}
