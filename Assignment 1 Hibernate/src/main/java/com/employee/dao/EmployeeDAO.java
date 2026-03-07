package com.employee.dao;

import com.employee.entity.Employee;
import com.employee.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Data Access Object for Employee entity.
 * Handles all CRUD operations with proper transaction management.
 */
public class EmployeeDAO {

    // ========================
    // INSERT - Add new employee
    // ========================

    /**
     * Inserts a new employee into the database.
     * @param employee the Employee object to persist
     */
    public void insertEmployee(Employee employee) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(employee);
            transaction.commit();
            System.out.println("✅ Employee inserted successfully! ID: " + employee.getId());
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

    // ========================
    // UPDATE - Update by employeeCode
    // ========================

    /**
     * Updates an existing employee's details using employeeCode.
     * @param employeeCode the unique employee code
     * @param updatedEmployee the Employee object with updated fields
     */
    public void updateEmployeeByCode(String employeeCode, Employee updatedEmployee) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.employeeCode = :code", Employee.class);
            query.setParameter("code", employeeCode);

            List<Employee> results = query.getResultList();

            if (results.isEmpty()) {
                System.out.println("⚠️ No employee found with code: " + employeeCode);
                transaction.rollback();
                return;
            }

            Employee existing = results.get(0);
            existing.setName(updatedEmployee.getName());
            existing.setEmail(updatedEmployee.getEmail());
            existing.setDesignation(updatedEmployee.getDesignation());
            existing.setDepartment(updatedEmployee.getDepartment());
            existing.setJoiningDate(updatedEmployee.getJoiningDate());

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

    // ========================
    // SOFT DELETE - Set active = false
    // ========================

    /**
     * Soft deletes an employee by setting active = false.
     * @param employeeCode the unique employee code
     */
    public void softDeleteEmployee(String employeeCode) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.employeeCode = :code", Employee.class);
            query.setParameter("code", employeeCode);

            List<Employee> results = query.getResultList();

            if (results.isEmpty()) {
                System.out.println("⚠️ No employee found with code: " + employeeCode);
                transaction.rollback();
                return;
            }

            Employee employee = results.get(0);
            employee.setActive(false);
            em.merge(employee);
            transaction.commit();
            System.out.println("✅ Employee soft-deleted (active = false) successfully!");
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

    // ========================
    // FETCH - By ID
    // ========================

    /**
     * Fetches an employee by their primary key ID.
     * @param id the employee ID
     * @return the Employee object, or null if not found
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

    // ========================
    // FETCH - By employeeCode
    // ========================

    /**
     * Fetches an employee by their unique employeeCode.
     * @param employeeCode the unique code
     * @return the Employee object, or null if not found
     */
    public Employee fetchByEmployeeCode(String employeeCode) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.employeeCode = :code", Employee.class);
            query.setParameter("code", employeeCode);

            List<Employee> results = query.getResultList();
            if (results.isEmpty()) {
                System.out.println("⚠️ No employee found with code: " + employeeCode);
                return null;
            }
            return results.get(0);
        } finally {
            em.close();
        }
    }

    // ========================
    // FETCH - All Active Employees (JPQL)
    // ========================

    /**
     * Fetches all active employees using JPQL.
     * @return list of active employees
     */
    public List<Employee> fetchAllActiveEmployees() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.active = true", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
