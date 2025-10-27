package com.mycompany.hiChatJpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * clase que nos permitira usar el patron singleton
 *
 * @author gatog
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT = "HiChatPU";
    private static EntityManagerFactory emf;

    private JpaUtil() {
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf.createEntityManager();
    }
}
