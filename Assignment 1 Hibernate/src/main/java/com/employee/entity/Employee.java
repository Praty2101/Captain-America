package com.employee.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entity class mapped to the 'employee' table.
 * Represents an employee in the system.
 */
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "designation")
    private String designation;

    @Column(name = "department")
    private String department;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "active")
    private boolean active = true;

    // ========================
    // Constructors
    // ========================

    public Employee() {
    }

    public Employee(String employeeCode, String name, String email,
                    String designation, String department, LocalDate joiningDate) {
        this.employeeCode = employeeCode;
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.department = department;
        this.joiningDate = joiningDate;
        this.active = true;
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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // ========================
    // toString
    // ========================

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-12s | %-20s | %-25s | %-15s | %-15s | %-12s | %-6s |",
            id, employeeCode, name, email, designation, department, joiningDate, active
        );
    }

    /**
     * Returns a detailed string representation.
     */
    public String toDetailString() {
        return "--------------------------------------------\n" +
               "  ID            : " + id + "\n" +
               "  Employee Code : " + employeeCode + "\n" +
               "  Name          : " + name + "\n" +
               "  Email         : " + email + "\n" +
               "  Designation   : " + designation + "\n" +
               "  Department    : " + department + "\n" +
               "  Joining Date  : " + joiningDate + "\n" +
               "  Active        : " + active + "\n" +
               "--------------------------------------------";
    }
}
