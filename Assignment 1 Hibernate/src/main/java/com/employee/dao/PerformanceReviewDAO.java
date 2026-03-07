package com.employee.dao;

import com.employee.entity.PerformanceReview;
import com.employee.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Data Access Object for PerformanceReview entity.
 * Handles all CRUD operations with proper transaction management.
 */
public class PerformanceReviewDAO {

    // ========================
    // INSERT - Add performance review
    // ========================

    /**
     * Inserts a new performance review.
     * @param review the PerformanceReview to persist
     */
    public void insertReview(PerformanceReview review) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(review);
            transaction.commit();
            System.out.println("✅ Performance review inserted successfully! ID: " + review.getId());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error inserting performance review: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // UPDATE - Update performance review
    // ========================

    /**
     * Updates an existing performance review by its ID.
     * @param id the review ID
     * @param updated the PerformanceReview with updated values
     */
    public void updateReview(Long id, PerformanceReview updated) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            PerformanceReview existing = em.find(PerformanceReview.class, id);
            if (existing == null) {
                System.out.println("⚠️ No performance review found with ID: " + id);
                transaction.rollback();
                return;
            }

            existing.setRating(updated.getRating());
            existing.setReviewDate(updated.getReviewDate());
            existing.setComments(updated.getComments());

            em.merge(existing);
            transaction.commit();
            System.out.println("✅ Performance review updated successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error updating performance review: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // DELETE - Remove performance review
    // ========================

    /**
     * Deletes a performance review by its ID.
     * @param id the review ID
     */
    public void deleteReview(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            PerformanceReview review = em.find(PerformanceReview.class, id);
            if (review == null) {
                System.out.println("⚠️ No performance review found with ID: " + id);
                transaction.rollback();
                return;
            }

            em.remove(review);
            transaction.commit();
            System.out.println("✅ Performance review deleted successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error deleting performance review: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ===================================
    // FETCH - All reviews for an employee
    // ===================================

    /**
     * Fetches all performance reviews for a given employeeCode.
     * @param employeeCode the employee code
     * @return list of performance reviews
     */
    public List<PerformanceReview> fetchAllByEmployee(String employeeCode) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            TypedQuery<PerformanceReview> query = em.createQuery(
                "SELECT p FROM PerformanceReview p WHERE p.employeeCode = :code",
                PerformanceReview.class);
            query.setParameter("code", employeeCode);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
