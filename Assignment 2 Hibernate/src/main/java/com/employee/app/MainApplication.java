package com.employee.app;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.AddressDAO;
import com.employee.entity.Employee;
import com.employee.entity.Address;
import com.employee.util.JPAUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application - Console-based Employee-Address Management System.
 * Covers all 5 questions:
 *   Q1: Entity Classes (Employee + Address with One-to-One mapping)
 *   Q2: CRUD Operations
 *   Q3: Mapping Operations
 *   Q4: JPQL Queries
 *   Q5: Validation (handled via Bean Validation annotations in entities)
 */
public class MainApplication {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final AddressDAO addressDAO = new AddressDAO();

    // ========================
    // MAIN METHOD
    // ========================

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  EMPLOYEE-ADDRESS MANAGEMENT SYSTEM (Hibernate JPA)       ║");
        System.out.println("║  Assignment 2: One-to-One Mapping, CRUD, JPQL, Validation ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> employeeCrudMenu();
                case 2 -> addressCrudMenu();
                case 3 -> mappingOperationsMenu();
                case 4 -> jpqlQueriesMenu();
                case 0 -> {
                    running = false;
                    System.out.println("\n🔒 Shutting down... Goodbye!");
                }
                default -> System.out.println("⚠️ Invalid choice! Please try again.");
            }
        }

        JPAUtil.shutdown();
        scanner.close();
    }

    // ========================
    // MAIN MENU
    // ========================

    private static void printMainMenu() {
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│              MAIN MENU                   │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. Employee CRUD Operations     (Q2)   │");
        System.out.println("│  2. Address CRUD Operations      (Q2)   │");
        System.out.println("│  3. Mapping Operations           (Q3)   │");
        System.out.println("│  4. JPQL Queries                 (Q4)   │");
        System.out.println("│  0. Exit                                │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.println("  [Q1: Entities defined | Q5: Validations active]");
    }

    // ================================================================
    //         QUESTION 2: EMPLOYEE CRUD OPERATIONS
    // ================================================================

    private static void employeeCrudMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n┌──────────────────────────────────────────┐");
            System.out.println("│      EMPLOYEE CRUD OPERATIONS            │");
            System.out.println("├──────────────────────────────────────────┤");
            System.out.println("│  1. Insert Employee with Address         │");
            System.out.println("│  2. Insert Employee (without Address)    │");
            System.out.println("│  3. Update Employee Details              │");
            System.out.println("│  4. Delete Employee by ID                │");
            System.out.println("│  5. Fetch Employee by ID                 │");
            System.out.println("│  6. Fetch All Employees                  │");
            System.out.println("│  0. Back to Main Menu                    │");
            System.out.println("└──────────────────────────────────────────┘");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> insertEmployeeWithAddress();
                case 2 -> insertEmployeeOnly();
                case 3 -> updateEmployee();
                case 4 -> deleteEmployee();
                case 5 -> fetchEmployeeById();
                case 6 -> fetchAllEmployees();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void insertEmployeeWithAddress() {
        System.out.println("\n--- Insert Employee with Address ---");

        // Employee details
        String name = readString("Employee Name: ");
        String email = readString("Email: ");
        String gender = readString("Gender (M/F/Other): ");
        String password = readString("Password: ");
        String phone = readString("Phone (10 digits): ");
        double salary = readDouble("Salary (must be > 0): ");
        LocalDate dob = readDate("Date of Birth (YYYY-MM-DD): ");

        // Address details
        System.out.println("\n  --- Address Details ---");
        String street = readString("Street: ");
        String city = readString("City: ");
        String state = readString("State: ");
        String country = readString("Country: ");
        String pincode = readString("Pincode (6 digits): ");

        Employee employee = new Employee(name, email, gender, password, phone, salary, dob);
        Address address = new Address(street, city, state, country, pincode);

        employeeDAO.insertEmployeeWithAddress(employee, address);
    }

    private static void insertEmployeeOnly() {
        System.out.println("\n--- Insert Employee (without Address) ---");
        String name = readString("Employee Name: ");
        String email = readString("Email: ");
        String gender = readString("Gender (M/F/Other): ");
        String password = readString("Password: ");
        String phone = readString("Phone (10 digits): ");
        double salary = readDouble("Salary (must be > 0): ");
        LocalDate dob = readDate("Date of Birth (YYYY-MM-DD): ");

        Employee employee = new Employee(name, email, gender, password, phone, salary, dob);
        employeeDAO.insertEmployee(employee);
    }

    private static void updateEmployee() {
        System.out.println("\n--- Update Employee ---");
        Long id = readLong("Enter Employee ID: ");

        Employee existing = employeeDAO.fetchById(id);
        if (existing == null) return;

        System.out.println("Current details:\n" + existing.toDetailString());

        String name = readString("New Name (current: " + existing.getEmployeeName() + "): ");
        String email = readString("New Email (current: " + existing.getEmail() + "): ");
        String gender = readString("New Gender (current: " + existing.getGender() + "): ");
        String password = readString("New Password: ");
        String phone = readString("New Phone (current: " + existing.getPhone() + "): ");
        double salary = readDouble("New Salary (current: " + existing.getSalary() + "): ");
        LocalDate dob = readDate("New DOB (YYYY-MM-DD, current: " + existing.getDateOfBirth() + "): ");

        Employee updated = new Employee();
        updated.setEmployeeName(name.isEmpty() ? existing.getEmployeeName() : name);
        updated.setEmail(email.isEmpty() ? existing.getEmail() : email);
        updated.setGender(gender.isEmpty() ? existing.getGender() : gender);
        updated.setPassword(password.isEmpty() ? existing.getPassword() : password);
        updated.setPhone(phone.isEmpty() ? existing.getPhone() : phone);
        updated.setSalary(salary > 0 ? salary : existing.getSalary());
        updated.setDateOfBirth(dob != null ? dob : existing.getDateOfBirth());

        employeeDAO.updateEmployee(id, updated);
    }

    private static void deleteEmployee() {
        System.out.println("\n--- Delete Employee ---");
        Long id = readLong("Enter Employee ID to delete: ");
        employeeDAO.deleteEmployee(id);
    }

    private static void fetchEmployeeById() {
        System.out.println("\n--- Fetch Employee by ID ---");
        Long id = readLong("Enter Employee ID: ");
        Employee emp = employeeDAO.fetchById(id);
        if (emp != null) {
            System.out.println(emp.toDetailString());
        }
    }

    private static void fetchAllEmployees() {
        System.out.println("\n--- All Employees ---");
        List<Employee> employees = employeeDAO.fetchAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("⚠️ No employees found.");
            return;
        }

        System.out.printf("| %-4s | %-20s | %-25s | %-8s | %-12s | %10s | %-12s |%n",
            "ID", "Name", "Email", "Gender", "Phone", "Salary", "DOB");
        System.out.println("-".repeat(110));
        for (Employee e : employees) {
            System.out.println(e);
        }
        System.out.println("\nTotal employees: " + employees.size());
    }

    // ================================================================
    //          QUESTION 2: ADDRESS CRUD OPERATIONS
    // ================================================================

    private static void addressCrudMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n┌──────────────────────────────────────────┐");
            System.out.println("│       ADDRESS CRUD OPERATIONS            │");
            System.out.println("├──────────────────────────────────────────┤");
            System.out.println("│  1. Insert Address for Employee          │");
            System.out.println("│  2. Update Address Details               │");
            System.out.println("│  3. Delete Address                       │");
            System.out.println("│  4. Fetch Address by ID                  │");
            System.out.println("│  0. Back to Main Menu                    │");
            System.out.println("└──────────────────────────────────────────┘");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> insertAddress();
                case 2 -> updateAddress();
                case 3 -> deleteAddress();
                case 4 -> fetchAddressById();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void insertAddress() {
        System.out.println("\n--- Insert Address for Employee ---");
        Long empId = readLong("Enter Employee ID: ");
        String street = readString("Street: ");
        String city = readString("City: ");
        String state = readString("State: ");
        String country = readString("Country: ");
        String pincode = readString("Pincode (6 digits): ");

        Address address = new Address(street, city, state, country, pincode);
        addressDAO.insertAddressForEmployee(empId, address);
    }

    private static void updateAddress() {
        System.out.println("\n--- Update Address ---");
        Long addrId = readLong("Enter Address ID: ");

        Address existing = addressDAO.fetchById(addrId);
        if (existing == null) return;

        System.out.println("Current: " + existing.toDetailString());

        String street = readString("New Street (current: " + existing.getStreet() + "): ");
        String city = readString("New City (current: " + existing.getCity() + "): ");
        String state = readString("New State (current: " + existing.getState() + "): ");
        String country = readString("New Country (current: " + existing.getCountry() + "): ");
        String pincode = readString("New Pincode (current: " + existing.getPincode() + "): ");

        Address updated = new Address();
        updated.setStreet(street.isEmpty() ? existing.getStreet() : street);
        updated.setCity(city.isEmpty() ? existing.getCity() : city);
        updated.setState(state.isEmpty() ? existing.getState() : state);
        updated.setCountry(country.isEmpty() ? existing.getCountry() : country);
        updated.setPincode(pincode.isEmpty() ? existing.getPincode() : pincode);

        addressDAO.updateAddress(addrId, updated);
    }

    private static void deleteAddress() {
        System.out.println("\n--- Delete Address ---");
        Long addrId = readLong("Enter Address ID to delete: ");
        addressDAO.deleteAddress(addrId);
    }

    private static void fetchAddressById() {
        System.out.println("\n--- Fetch Address by ID ---");
        Long addrId = readLong("Enter Address ID: ");
        Address addr = addressDAO.fetchById(addrId);
        if (addr != null) {
            System.out.println(addr.toDetailString());
        }
    }

    // ================================================================
    //          QUESTION 3: MAPPING OPERATIONS
    // ================================================================

    private static void mappingOperationsMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n┌──────────────────────────────────────────────────┐");
            System.out.println("│           MAPPING OPERATIONS (Q3)                │");
            System.out.println("├──────────────────────────────────────────────────┤");
            System.out.println("│  1. Save Employee with Address (single txn)      │");
            System.out.println("│  2. Fetch Employee with Address                  │");
            System.out.println("│  3. Update Employee Salary & Address City        │");
            System.out.println("│  4. Delete Employee & Verify Cascade Delete      │");
            System.out.println("│  0. Back to Main Menu                            │");
            System.out.println("└──────────────────────────────────────────────────┘");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> insertEmployeeWithAddress();
                case 2 -> fetchWithAddress();
                case 3 -> updateSalaryAndCity();
                case 4 -> deleteCascade();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void fetchWithAddress() {
        System.out.println("\n--- Fetch Employee with Address ---");
        Long id = readLong("Enter Employee ID: ");
        Employee emp = employeeDAO.fetchEmployeeWithAddress(id);
        if (emp != null) {
            System.out.println(emp.toDetailString());
        }
    }

    private static void updateSalaryAndCity() {
        System.out.println("\n--- Update Salary & Address City ---");
        Long id = readLong("Enter Employee ID: ");
        double newSalary = readDouble("New Salary: ");
        String newCity = readString("New City: ");
        employeeDAO.updateSalaryAndCity(id, newSalary, newCity);
    }

    private static void deleteCascade() {
        System.out.println("\n--- Delete Employee & Verify Cascade ---");
        Long id = readLong("Enter Employee ID to delete: ");
        employeeDAO.deleteAndVerifyCascade(id);
    }

    // ================================================================
    //              QUESTION 4: JPQL QUERIES
    // ================================================================

    private static void jpqlQueriesMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n┌──────────────────────────────────────────────────┐");
            System.out.println("│              JPQL QUERIES (Q4)                   │");
            System.out.println("├──────────────────────────────────────────────────┤");
            System.out.println("│  1. Employees with Salary > 15000               │");
            System.out.println("│  2. Employees from a Specific City              │");
            System.out.println("│  3. Employees Born After a Given Date           │");
            System.out.println("│  4. Count Employees in Each City                │");
            System.out.println("│  0. Back to Main Menu                           │");
            System.out.println("└──────────────────────────────────────────────────┘");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> fetchHighSalaryEmployees();
                case 2 -> fetchEmployeesByCity();
                case 3 -> fetchEmployeesByDOB();
                case 4 -> countByCity();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void fetchHighSalaryEmployees() {
        System.out.println("\n--- Employees with Salary > 15000 ---");
        List<Employee> employees = employeeDAO.fetchBySalaryGreaterThan(15000);

        if (employees.isEmpty()) {
            System.out.println("⚠️ No employees found with salary > 15000.");
            return;
        }

        for (Employee e : employees) {
            System.out.println(e.toDetailString());
        }
        System.out.println("Total: " + employees.size());
    }

    private static void fetchEmployeesByCity() {
        System.out.println("\n--- Employees from a City ---");
        String city = readString("Enter City: ");
        List<Employee> employees = employeeDAO.fetchByCity(city);

        if (employees.isEmpty()) {
            System.out.println("⚠️ No employees found in city: " + city);
            return;
        }

        for (Employee e : employees) {
            System.out.println(e.toDetailString());
        }
        System.out.println("Total: " + employees.size());
    }

    private static void fetchEmployeesByDOB() {
        System.out.println("\n--- Employees Born After a Date ---");
        LocalDate date = readDate("Enter Date (YYYY-MM-DD): ");
        if (date == null) {
            System.out.println("⚠️ Invalid date.");
            return;
        }

        List<Employee> employees = employeeDAO.fetchBornAfter(date);

        if (employees.isEmpty()) {
            System.out.println("⚠️ No employees found born after: " + date);
            return;
        }

        for (Employee e : employees) {
            System.out.println(e.toDetailString());
        }
        System.out.println("Total: " + employees.size());
    }

    private static void countByCity() {
        System.out.println("\n--- Employee Count per City ---");
        List<Object[]> results = employeeDAO.countEmployeesPerCity();

        if (results.isEmpty()) {
            System.out.println("⚠️ No data found.");
            return;
        }

        System.out.printf("| %-20s | %-10s |%n", "City", "Count");
        System.out.println("-".repeat(36));
        for (Object[] row : results) {
            System.out.printf("| %-20s | %-10s |%n", row[0], row[1]);
        }
    }

    // ========================
    // INPUT UTILITY METHODS
    // ========================

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid integer.");
            }
        }
    }

    private static long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid decimal number.");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return null;
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("⚠️ Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }
}
