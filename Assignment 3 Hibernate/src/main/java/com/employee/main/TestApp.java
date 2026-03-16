package com.employee.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.employee.service.EmployeeService;

public class TestApp {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        EmployeeService service =
                (EmployeeService) context.getBean("employeeService");

        service.addEmployee();
    }
}