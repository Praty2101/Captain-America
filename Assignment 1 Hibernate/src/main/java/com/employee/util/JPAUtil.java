package com.employee.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing JPA EntityManagerFactory.
 * Provides a singleton EntityManagerFactory instance.
 */
public class JPAUtil {

    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("EmployeePU");
        } catch (Exception e) {
            System.err.println("ERROR: EntityManagerFactory creation failed!");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Returns the singleton EntityManagerFactory instance.
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Shuts down the EntityManagerFactory.
     */
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
