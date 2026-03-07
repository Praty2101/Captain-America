package com.employee.dao;

import com.employee.entity.Address;
import com.employee.entity.Employee;
import com.employee.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * Data Access Object for Employee entity.
 * Handles CRUD operations, mapping operations, and JPQL queries.
 * All operations use proper transaction management.
 */
public class EmployeeDAO {

    // ================================================================
    //              QUESTION 2: CRUD OPERATIONS
    // ================================================================

    /**
     * Inserts a new employee along with their address in a single transaction.
     * (Also satisfies Q3: Save employee with address in a single transaction)
     */
    public void insertEmployeeWithAddress(Employee employee, Address address) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Set bidirectional relationship
            employee.setAddress(address);

            // Persist employee (address cascades due to CascadeType.ALL)
            em.persist(employee);

            transaction.commit();
            System.out.println("✅ Employee inserted with address! Employee ID: " + employee.getId());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error inserting employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Inserts a new employee without address.
     */
    public void insertEmployee(Employee employee) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(employee);
            transaction.commit();
            System.out.println("✅ Employee inserted! ID: " + employee.getId());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error inserting employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Updates an existing employee's details by ID.
     */
    public void updateEmployee(Long id, Employee updated) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Employee existing = em.find(Employee.class, id);
            if (existing == null) {
                System.out.println("⚠️ No employee found with ID: " + id);
                transaction.rollback();
                return;
            }

            existing.setEmployeeName(updated.getEmployeeName());
            existing.setEmail(updated.getEmail());
            existing.setGender(updated.getGender());
            existing.setPassword(updated.getPassword());
            existing.setPhone(updated.getPhone());
            existing.setSalary(updated.getSalary());
            existing.setDateOfBirth(updated.getDateOfBirth());

            em.merge(existing);
            transaction.commit();
            System.out.println("✅ Employee updated successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error updating employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Deletes an employee by ID.
     * Address is deleted automatically due to CascadeType.ALL + orphanRemoval.
     */
    public void deleteEmployee(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Employee employee = em.find(Employee.class, id);
            if (employee == null) {
                System.out.println("⚠️ No employee found with ID: " + id);
                transaction.rollback();
                return;
            }

            em.remove(employee);
            transaction.commit();
            System.out.println("✅ Employee deleted! (Address also deleted automatically via cascade)");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error deleting employee: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Fetches an employee by ID (with address via EAGER fetch).
     */
    public Employee fetchById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            Employee employee = em.find(Employee.class, id);
            if (employee == null) {
                System.out.println("⚠️ No employee found with ID: " + id);
            }
            return employee;
        } finally {
            em.close();
        }
    }

    /**
     * Fetches all employees.
     */
    public List<Employee> fetchAllEmployees() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ================================================================
    //      QUESTION 3: MAPPING OPERATIONS
    // ================================================================

    /**
     * Q3: Fetch employee with address.
     */
    public Employee fetchEmployeeWithAddress(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e LEFT JOIN FETCH e.address WHERE e.id = :id",
                Employee.class);
            query.setParameter("id", id);

            List<Employee> results = query.getResultList();
            if (results.isEmpty()) {
                System.out.println("⚠️ No employee found with ID: " + id);
                return null;
            }
            return results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Q3: Update employee salary and address city in a single transaction.
     */
    public void updateSalaryAndCity(Long employeeId, double newSalary, String newCity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Employee employee = em.find(Employee.class, employeeId);
            if (employee == null) {
                System.out.println("⚠️ No employee found with ID: " + employeeId);
                transaction.rollback();
                return;
            }

            employee.setSalary(newSalary);

            if (employee.getAddress() != null) {
                employee.getAddress().setCity(newCity);
            } else {
                System.out.println("⚠️ Employee has no address to update city.");
            }

            em.merge(employee);
            transaction.commit();
            System.out.println("✅ Salary updated to " + String.format("%.2f", newSalary)
                + " and city updated to '" + newCity + "'");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error updating salary and city: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Q3: Delete employee and verify if address is also deleted (cascade test).
     */
    public void deleteAndVerifyCascade(Long employeeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Employee employee = em.find(Employee.class, employeeId);
            if (employee == null) {
                System.out.println("⚠️ No employee found with ID: " + employeeId);
                transaction.rollback();
                return;
            }

            Long addressId = (employee.getAddress() != null) ? employee.getAddress().getId() : null;
            System.out.println("🔍 Before delete - Employee ID: " + employeeId
                + ", Address ID: " + addressId);

            em.remove(employee);
            transaction.commit();

            System.out.println("✅ Employee deleted!");

            // Verify cascade delete of address
            if (addressId != null) {
                EntityManager em2 = JPAUtil.getEntityManagerFactory().createEntityManager();
                Address addr = em2.find(Address.class, addressId);
                if (addr == null) {
                    System.out.println("✅ Address (ID: " + addressId
                        + ") was ALSO deleted automatically (cascade verified!)");
                } else {
                    System.out.println("⚠️ Address (ID: " + addressId + ") still exists.");
                }
                em2.close();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ================================================================
    //              QUESTION 4: JPQL QUERIES
    // ================================================================

    /**
     * Q4: Fetch employees with salary greater than a given amount.
     */
    public List<Employee> fetchBySalaryGreaterThan(double minSalary) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.salary > :minSalary", Employee.class);
            query.setParameter("minSalary", minSalary);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Q4: Fetch employees from a specific city.
     */
    public List<Employee> fetchByCity(String city) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e JOIN e.address a WHERE a.city = :city", Employee.class);
            query.setParameter("city", city);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Q4: Fetch employees born after a given date.
     */
    public List<Employee> fetchBornAfter(LocalDate date) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.dateOfBirth > :date", Employee.class);
            query.setParameter("date", date);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Q4: Count number of employees in each city.
     */
    public List<Object[]> countEmployeesPerCity() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT a.city, COUNT(e) FROM Employee e JOIN e.address a GROUP BY a.city",
                Object[].class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
