package com.employee.model;

public class Employee {

    private int id;
    private String name;
    private String email;
    private String password;
    private int age;
    private double salary;
    private Address address;

    public Employee(Address address) {
        this.address = address;
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setAge(int age) { this.age = age; }
    public void setSalary(double salary) { this.salary = salary; }

    public void display() {
        System.out.println("Employee ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Age: " + age);
        System.out.println("Salary: " + salary);

        System.out.println("City: " + address.getCity());
        System.out.println("State: " + address.getState());
        System.out.println("Country: " + address.getCountry());
        System.out.println("Pincode: " + address.getPincode());
    }
}