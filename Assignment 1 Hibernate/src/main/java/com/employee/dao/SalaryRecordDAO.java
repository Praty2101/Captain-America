package com.employee.dao;

import com.employee.entity.SalaryRecord;
import com.employee.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Data Access Object for SalaryRecord entity.
 * Handles all CRUD operations with proper transaction management.
 * Note: employeeCode is treated as a normal column (no FK mapping).
 */
public class SalaryRecordDAO {

    // ========================
    // INSERT - Add salary record
    // ========================

    /**
     * Inserts a new salary record. netSalary is auto-calculated.
     * @param salaryRecord the SalaryRecord to persist
     */
    public void insertSalaryRecord(SalaryRecord salaryRecord) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(salaryRecord);
            transaction.commit();
            System.out.println("✅ Salary record inserted successfully! ID: "
                + salaryRecord.getId()
                + " | Net Salary: " + String.format("%.2f", salaryRecord.getNetSalary()));
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error inserting salary record: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // UPDATE - Update salary record
    // ========================

    /**
     * Updates an existing salary record by its ID.
     * netSalary will be auto-recalculated on update.
     * @param id the salary record ID
     * @param updated the SalaryRecord with updated values
     */
    public void updateSalaryRecord(Long id, SalaryRecord updated) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            SalaryRecord existing = em.find(SalaryRecord.class, id);
            if (existing == null) {
                System.out.println("⚠️ No salary record found with ID: " + id);
                transaction.rollback();
                return;
            }

            existing.setBaseSalary(updated.getBaseSalary());
            existing.setBonus(updated.getBonus());
            existing.setTax(updated.getTax());
            existing.setSalaryMonth(updated.getSalaryMonth());
            // netSalary auto-calculated via @PreUpdate

            em.merge(existing);
            transaction.commit();
            System.out.println("✅ Salary record updated successfully! Net Salary: "
                + String.format("%.2f", existing.getNetSalary()));
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error updating salary record: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // DELETE - Remove salary record
    // ========================

    /**
     * Deletes a salary record by its ID.
     * @param id the salary record ID
     */
    public void deleteSalaryRecord(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            SalaryRecord salaryRecord = em.find(SalaryRecord.class, id);
            if (salaryRecord == null) {
                System.out.println("⚠️ No salary record found with ID: " + id);
                transaction.rollback();
                return;
            }

            em.remove(salaryRecord);
            transaction.commit();
            System.out.println("✅ Salary record deleted successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error deleting salary record: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // FETCH - By employeeCode
    // ========================

    /**
     * Fetches all salary records for a given employeeCode.
     * @param employeeCode the employee code
     * @return list of salary records
     */
    public List<SalaryRecord> fetchByEmployeeCode(String employeeCode) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<SalaryRecord> query = em.createQuery(
                "SELECT s FROM SalaryRecord s WHERE s.employeeCode = :code", SalaryRecord.class);
            query.setParameter("code", employeeCode);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ========================
    // FETCH - By salaryMonth
    // ========================

    /**
     * Fetches all salary records for a given salary month.
     * @param salaryMonth the month in YYYY-MM format
     * @return list of salary records for that month
     */
    public List<SalaryRecord> fetchBySalaryMonth(String salaryMonth) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<SalaryRecord> query = em.createQuery(
                "SELECT s FROM SalaryRecord s WHERE s.salaryMonth = :month", SalaryRecord.class);
            query.setParameter("month", salaryMonth);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ================================================
    // FETCH - All salary records for a given employee
    // ================================================

    /**
     * Fetches all salary records for a given employee (same as fetchByEmployeeCode).
     * @param employeeCode the employee code
     * @return list of all salary records for the employee
     */
    public List<SalaryRecord> fetchAllByEmployee(String employeeCode) {
        return fetchByEmployeeCode(employeeCode);
    }
}
