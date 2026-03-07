package com.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entity class mapped to the 'address' table.
 * Has a One-to-One relationship with Employee.
 * 
 * Validations:
 *   - Pincode must contain exactly 6 digits
 */
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    @NotBlank(message = "City cannot be blank")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "pincode")
    @Pattern(regexp = "^\\d{6}$", message = "Pincode must contain exactly 6 digits")
    private String pincode;

    /**
     * One-to-One mapping with Employee.
     * This is the owning side of the relationship.
     * @JoinColumn creates a foreign key column 'employee_id' in the address table.
     */
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    // ========================
    // Constructors
    // ========================

    public Address() {
    }

    public Address(String street, String city, String state, String country, String pincode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    // ========================
    // toString
    // ========================

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-20s | %-15s | %-15s | %-15s | %-8s |",
            id, street, city, state, country, pincode
        );
    }

    public String toDetailString() {
        return "--------------------------------------------\n" +
               "  Address ID : " + id + "\n" +
               "  Street     : " + street + "\n" +
               "  City       : " + city + "\n" +
               "  State      : " + state + "\n" +
               "  Country    : " + country + "\n" +
               "  Pincode    : " + pincode + "\n" +
               "--------------------------------------------";
    }
}
