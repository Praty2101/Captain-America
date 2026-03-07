package com.employee.entity;

import jakarta.persistence.*;

/**
 * Entity class mapped to the 'salary_record' table.
 * employeeCode is stored as a normal column (NO foreign key mapping).
 * netSalary is automatically calculated via @PrePersist and @PreUpdate.
 */
@Entity
@Table(name = "salary_record")
public class SalaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @Column(name = "base_salary", nullable = false)
    private double baseSalary;

    @Column(name = "bonus")
    private double bonus;

    @Column(name = "tax")
    private double tax;

    @Column(name = "net_salary")
    private double netSalary;

    @Column(name = "salary_month", nullable = false)
    private String salaryMonth; // Format: YYYY-MM

    // ========================
    // Constructors
    // ========================

    public SalaryRecord() {
    }

    public SalaryRecord(String employeeCode, double baseSalary, double bonus,
                        double tax, String salaryMonth) {
        this.employeeCode = employeeCode;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.tax = tax;
        this.salaryMonth = salaryMonth;
        calculateNetSalary();
    }

    // ========================
    // Auto-calculate netSalary
    // ========================

    /**
     * Automatically calculates netSalary before persisting or updating.
     * Formula: netSalary = baseSalary + bonus - tax
     */
    @PrePersist
    @PreUpdate
    public void calculateNetSalary() {
        this.netSalary = this.baseSalary + this.bonus - this.tax;
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

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public String getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    // ========================
    // toString
    // ========================

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-12s | %12.2f | %10.2f | %10.2f | %12.2f | %-10s |",
            id, employeeCode, baseSalary, bonus, tax, netSalary, salaryMonth
        );
    }

    /**
     * Returns a detailed string representation.
     */
    public String toDetailString() {
        return "--------------------------------------------\n" +
               "  ID            : " + id + "\n" +
               "  Employee Code : " + employeeCode + "\n" +
               "  Base Salary   : " + String.format("%.2f", baseSalary) + "\n" +
               "  Bonus         : " + String.format("%.2f", bonus) + "\n" +
               "  Tax           : " + String.format("%.2f", tax) + "\n" +
               "  Net Salary    : " + String.format("%.2f", netSalary) + "\n" +
               "  Salary Month  : " + salaryMonth + "\n" +
               "--------------------------------------------";
    }
}
