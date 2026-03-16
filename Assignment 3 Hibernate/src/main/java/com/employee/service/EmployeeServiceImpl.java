package com.employee.service;

import com.employee.model.Employee;

public class EmployeeServiceImpl implements EmployeeService {

    private Employee employee;

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void addEmployee() {
        System.out.println("Employee Added Successfully");
        employee.display();
    }

    @Override
    public void updateEmployee() {
        System.out.println("Employee Updated");
    }

    @Override
    public void deleteEmployee() {
        System.out.println("Employee Deleted");
    }

    @Override
    public void getEmployee() {
        System.out.println("Fetching Employee Details");
        employee.display();
    }
}