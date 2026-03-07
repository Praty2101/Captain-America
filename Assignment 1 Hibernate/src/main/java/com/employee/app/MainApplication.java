package com.employee.app;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.SalaryRecordDAO;
import com.employee.dao.PerformanceReviewDAO;
import com.employee.entity.Employee;
import com.employee.entity.SalaryRecord;
import com.employee.entity.PerformanceReview;
import com.employee.util.JPAUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application - Console-based Employee Management System.
 * Provides menu-driven interface for:
 *   1. Employee Management
 *   2. Salary Record Management
 *   3. Performance Review Management
 */
public class MainApplication {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final SalaryRecordDAO salaryRecordDAO = new SalaryRecordDAO();
    private static final PerformanceReviewDAO reviewDAO = new PerformanceReviewDAO();

    // ========================
    // MAIN METHOD
    // ========================

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║    EMPLOYEE MANAGEMENT SYSTEM (Hibernate JPA)   ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> employeeMenu();
                case 2 -> salaryMenu();
                case 3 -> performanceReviewMenu();
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
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│         MAIN MENU                │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Employee Management          │");
        System.out.println("│  2. Salary Record Management     │");
        System.out.println("│  3. Performance Review Management│");
        System.out.println("│  0. Exit                         │");
        System.out.println("└──────────────────────────────────┘");
    }

    // ================================================================
    //                  QUESTION 1: EMPLOYEE MANAGEMENT
    // ================================================================

    private static void employeeMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n┌──────────────────────────────────────┐");
            System.out.println("│       EMPLOYEE MANAGEMENT            │");
            System.out.println("├──────────────────────────────────────┤");
            System.out.println("│  1. Insert New Employee              │");
            System.out.println("│  2. Update Employee (by Code)        │");
            System.out.println("│  3. Soft Delete Employee             │");
            System.out.println("│  4. Fetch Employee by ID             │");
            System.out.println("│  5. Fetch Employee by Employee Code  │");
            System.out.println("│  6. Fetch All Active Employees       │");
            System.out.println("│  0. Back to Main Menu                │");
            System.out.println("└──────────────────────────────────────┘");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> insertEmployee();
                case 2 -> updateEmployee();
                case 3 -> softDeleteEmployee();
                case 4 -> fetchEmployeeById();
                case 5 -> fetchEmployeeByCode();
                case 6 -> fetchAllActiveEmployees();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void insertEmployee() {
        System.out.println("\n--- Insert New Employee ---");
        String code = readString("Employee Code: ");
        String name = readString("Name: ");
        String email = readString("Email: ");
        String designation = readString("Designation: ");
        String department = readString("Department: ");
        LocalDate joiningDate = readDate("Joining Date (YYYY-MM-DD): ");

        Employee employee = new Employee(code, name, email, designation, department, joiningDate);
        employeeDAO.insertEmployee(employee);
    }

    private static void updateEmployee() {
        System.out.println("\n--- Update Employee by Code ---");
        String code = readString("Enter Employee Code to update: ");

        Employee existing = employeeDAO.fetchByEmployeeCode(code);
        if (existing == null) return;

        System.out.println("Current details:\n" + existing.toDetailString());

        String name = readString("New Name (current: " + existing.getName() + "): ");
        String email = readString("New Email (current: " + existing.getEmail() + "): ");
        String designation = readString("New Designation (current: " + existing.getDesignation() + "): ");
        String department = readString("New Department (current: " + existing.getDepartment() + "): ");
        LocalDate joiningDate = readDate("New Joining Date (YYYY-MM-DD, current: " + existing.getJoiningDate() + "): ");

        Employee updated = new Employee();
        updated.setName(name.isEmpty() ? existing.getName() : name);
        updated.setEmail(email.isEmpty() ? existing.getEmail() : email);
        updated.setDesignation(designation.isEmpty() ? existing.getDesignation() : designation);
        updated.setDepartment(department.isEmpty() ? existing.getDepartment() : department);
        updated.setJoiningDate(joiningDate != null ? joiningDate : existing.getJoiningDate());

        employeeDAO.updateEmployeeByCode(code, updated);
    }

    private static void softDeleteEmployee() {
        System.out.println("\n--- Soft Delete Employee ---");
        String code = readString("Enter Employee Code to soft-delete: ");
        employeeDAO.softDeleteEmployee(code);
    }

    private static void fetchEmployeeById() {
        System.out.println("\n--- Fetch Employee by ID ---");
        Long id = readLong("Enter Employee ID: ");
        Employee emp = employeeDAO.fetchById(id);
        if (emp != null) {
            System.out.println(emp.toDetailString());
        }
    }

    private static void fetchEmployeeByCode() {
        System.out.println("\n--- Fetch Employee by Code ---");
        String code = readString("Enter Employee Code: ");
        Employee emp = employeeDAO.fetchByEmployeeCode(code);
        if (emp != null) {
            System.out.println(emp.toDetailString());
        }
    }

    private static void fetchAllActiveEmployees() {
        System.out.println("\n--- All Active Employees ---");
        List<Employee> employees = employeeDAO.fetchAllActiveEmployees();

        if (employees.isEmpty()) {
            System.out.println("⚠️ No active employees found.");
            return;
        }

        System.out.printf("| %-4s | %-12s | %-20s | %-25s | %-15s | %-15s | %-12s | %-6s |%n",
            "ID", "Code", "Name", "Email", "Designation", "Department", "Joining", "Active");
        System.out.println("-".repeat(130));
        for (Employee e : employees) {
            System.out.println(e);
        }
        System.out.println("\nTotal active employees: " + employees.size());
    }

    // ================================================================
    //              QUESTION 2: SALARY RECORD MANAGEMENT
    // ================================================================

    private static void salaryMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n┌──────────────────────────────────────────┐");
            System.out.println("│       SALARY RECORD MANAGEMENT           │");
            System.out.println("├──────────────────────────────────────────┤");
            System.out.println("│  1. Insert Salary Record                 │");
            System.out.println("│  2. Update Salary Record                 │");
            System.out.println("│  3. Delete Salary Record                 │");
            System.out.println("│  4. Fetch by Employee Code               │");
            System.out.println("│  5. Fetch by Salary Month                │");
            System.out.println("│  6. Fetch All Records for an Employee    │");
            System.out.println("│  0. Back to Main Menu                    │");
            System.out.println("└──────────────────────────────────────────┘");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> insertSalaryRecord();
                case 2 -> updateSalaryRecord();
                case 3 -> deleteSalaryRecord();
                case 4 -> fetchSalaryByCode();
                case 5 -> fetchSalaryByMonth();
                case 6 -> fetchAllSalaryForEmployee();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void insertSalaryRecord() {
        System.out.println("\n--- Insert Salary Record ---");
        String code = readString("Employee Code: ");
        double baseSalary = readDouble("Base Salary: ");
        double bonus = readDouble("Bonus: ");
        double tax = readDouble("Tax: ");
        String month = readString("Salary Month (YYYY-MM): ");

        SalaryRecord record = new SalaryRecord(code, baseSalary, bonus, tax, month);
        salaryRecordDAO.insertSalaryRecord(record);
    }

    private static void updateSalaryRecord() {
        System.out.println("\n--- Update Salary Record ---");
        Long id = readLong("Enter Salary Record ID to update: ");
        double baseSalary = readDouble("New Base Salary: ");
        double bonus = readDouble("New Bonus: ");
        double tax = readDouble("New Tax: ");
        String month = readString("New Salary Month (YYYY-MM): ");

        SalaryRecord updated = new SalaryRecord();
        updated.setBaseSalary(baseSalary);
        updated.setBonus(bonus);
        updated.setTax(tax);
        updated.setSalaryMonth(month);

        salaryRecordDAO.updateSalaryRecord(id, updated);
    }

    private static void deleteSalaryRecord() {
        System.out.println("\n--- Delete Salary Record ---");
        Long id = readLong("Enter Salary Record ID to delete: ");
        salaryRecordDAO.deleteSalaryRecord(id);
    }

    private static void fetchSalaryByCode() {
        System.out.println("\n--- Fetch Salary Records by Employee Code ---");
        String code = readString("Enter Employee Code: ");
        List<SalaryRecord> records = salaryRecordDAO.fetchByEmployeeCode(code);

        if (records.isEmpty()) {
            System.out.println("⚠️ No salary records found for code: " + code);
            return;
        }

        printSalaryHeader();
        records.forEach(System.out::println);
    }

    private static void fetchSalaryByMonth() {
        System.out.println("\n--- Fetch Salary Records by Month ---");
        String month = readString("Enter Salary Month (YYYY-MM): ");
        List<SalaryRecord> records = salaryRecordDAO.fetchBySalaryMonth(month);

        if (records.isEmpty()) {
            System.out.println("⚠️ No salary records found for month: " + month);
            return;
        }

        printSalaryHeader();
        records.forEach(System.out::println);
    }

    private static void fetchAllSalaryForEmployee() {
        System.out.println("\n--- All Salary Records for Employee ---");
        String code = readString("Enter Employee Code: ");
        List<SalaryRecord> records = salaryRecordDAO.fetchAllByEmployee(code);

        if (records.isEmpty()) {
            System.out.println("⚠️ No salary records found for code: " + code);
            return;
        }

        printSalaryHeader();
        records.forEach(r -> System.out.println(r.toDetailString()));
    }

    private static void printSalaryHeader() {
        System.out.printf("| %-4s | %-12s | %12s | %10s | %10s | %12s | %-10s |%n",
            "ID", "Emp Code", "Base Salary", "Bonus", "Tax", "Net Salary", "Month");
        System.out.println("-".repeat(90));
    }

    // ================================================================
    //          QUESTION 3: PERFORMANCE REVIEW MANAGEMENT
    // ================================================================

    private static void performanceReviewMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n┌──────────────────────────────────────────┐");
            System.out.println("│    PERFORMANCE REVIEW MANAGEMENT         │");
            System.out.println("├──────────────────────────────────────────┤");
            System.out.println("│  1. Insert Performance Review            │");
            System.out.println("│  2. Update Performance Review            │");
            System.out.println("│  3. Delete Performance Review            │");
            System.out.println("│  4. Fetch All Reviews for an Employee    │");
            System.out.println("│  0. Back to Main Menu                    │");
            System.out.println("└──────────────────────────────────────────┘");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> insertReview();
                case 2 -> updateReview();
                case 3 -> deleteReview();
                case 4 -> fetchAllReviewsForEmployee();
                case 0 -> back = true;
                default -> System.out.println("⚠️ Invalid choice!");
            }
        }
    }

    private static void insertReview() {
        System.out.println("\n--- Insert Performance Review ---");
        String code = readString("Employee Code: ");
        int rating = readInt("Rating (1-5): ");
        LocalDate reviewDate = readDate("Review Date (YYYY-MM-DD): ");
        String comments = readString("Comments: ");

        try {
            PerformanceReview review = new PerformanceReview(code, rating, reviewDate, comments);
            reviewDAO.insertReview(review);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void updateReview() {
        System.out.println("\n--- Update Performance Review ---");
        Long id = readLong("Enter Review ID to update: ");
        int rating = readInt("New Rating (1-5): ");
        LocalDate reviewDate = readDate("New Review Date (YYYY-MM-DD): ");
        String comments = readString("New Comments: ");

        try {
            PerformanceReview updated = new PerformanceReview();
            updated.setRating(rating);
            updated.setReviewDate(reviewDate);
            updated.setComments(comments);
            reviewDAO.updateReview(id, updated);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void deleteReview() {
        System.out.println("\n--- Delete Performance Review ---");
        Long id = readLong("Enter Review ID to delete: ");
        reviewDAO.deleteReview(id);
    }

    private static void fetchAllReviewsForEmployee() {
        System.out.println("\n--- All Performance Reviews for Employee ---");
        String code = readString("Enter Employee Code: ");
        List<PerformanceReview> reviews = reviewDAO.fetchAllByEmployee(code);

        if (reviews.isEmpty()) {
            System.out.println("⚠️ No reviews found for code: " + code);
            return;
        }

        System.out.printf("| %-4s | %-12s | %-6s | %-12s | %-30s |%n",
            "ID", "Emp Code", "Rating", "Review Date", "Comments");
        System.out.println("-".repeat(80));
        reviews.forEach(r -> System.out.println(r.toDetailString()));
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
