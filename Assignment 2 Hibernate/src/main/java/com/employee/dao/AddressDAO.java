package com.employee.dao;

import com.employee.entity.Address;
import com.employee.entity.Employee;
import com.employee.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Data Access Object for Address entity.
 * Handles CRUD operations with proper transaction management.
 */
public class AddressDAO {

    // ========================
    // INSERT - Insert address for an employee
    // ========================

    /**
     * Inserts an address for an existing employee.
     * @param employeeId the employee's ID
     * @param address the Address object to persist
     */
    public void insertAddressForEmployee(Long employeeId, Address address) {
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

            if (employee.getAddress() != null) {
                System.out.println("⚠️ Employee already has an address. Use update instead.");
                transaction.rollback();
                return;
            }

            // Set bidirectional relationship
            address.setEmployee(employee);
            employee.setAddress(address);

            em.merge(employee);
            transaction.commit();
            System.out.println("✅ Address inserted for employee ID: " + employeeId);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error inserting address: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // UPDATE - Update address details
    // ========================

    /**
     * Updates an existing address by its ID.
     * @param addressId the address ID
     * @param updated the Address object with updated values
     */
    public void updateAddress(Long addressId, Address updated) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Address existing = em.find(Address.class, addressId);
            if (existing == null) {
                System.out.println("⚠️ No address found with ID: " + addressId);
                transaction.rollback();
                return;
            }

            existing.setStreet(updated.getStreet());
            existing.setCity(updated.getCity());
            existing.setState(updated.getState());
            existing.setCountry(updated.getCountry());
            existing.setPincode(updated.getPincode());

            em.merge(existing);
            transaction.commit();
            System.out.println("✅ Address updated successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error updating address: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // DELETE - Delete address
    // ========================

    /**
     * Deletes an address by its ID.
     * Also removes the reference from the owning employee.
     * @param addressId the address ID
     */
    public void deleteAddress(Long addressId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Address address = em.find(Address.class, addressId);
            if (address == null) {
                System.out.println("⚠️ No address found with ID: " + addressId);
                transaction.rollback();
                return;
            }

            // Remove the reference from employee side
            Employee employee = address.getEmployee();
            if (employee != null) {
                employee.setAddress(null);
            }

            em.remove(address);
            transaction.commit();
            System.out.println("✅ Address deleted successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("❌ Error deleting address: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========================
    // FETCH - Fetch address by ID
    // ========================

    /**
     * Fetches an address by its ID.
     * @param addressId the address ID
     * @return the Address object, or null if not found
     */
    public Address fetchById(Long addressId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();

        try {
            Address address = em.find(Address.class, addressId);
            if (address == null) {
                System.out.println("⚠️ No address found with ID: " + addressId);
            }
            return address;
        } finally {
            em.close();
        }
    }
}
