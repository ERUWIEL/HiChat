package com.mycompany.hiChatJpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


/**
 * clase que nos permitira usar el patron singleton implementando logs de
 * control para auditoriar su funcionamiento
 *
 * @author gatog
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT = "HiChatPU";

    private static volatile EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    private JpaUtil() {
    }

    /**
     * obtiene el EntityManagerFactory Thread-safe con double-check locking
     *
     * @return EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        EntityManagerFactory entityManagerFactory = JpaUtil.emf;
        if (entityManagerFactory == null) {
            synchronized (JpaUtil.class) {
                entityManagerFactory = JpaUtil.emf;
                if (entityManagerFactory == null) {
                    try {
                        JpaUtil.emf = entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                    } catch (Exception e) {
                        throw new RuntimeException("No se pudo inicializar la conexión a la base de datos", e);
                    }
                }
            }
        }
        return entityManagerFactory;
    }

    /**
     * obtiene el EntityManager para el hilo actual
     *
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();
        if (em == null || !em.isOpen()) {
            em = getEntityManagerFactory().createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    /**
     * metodo que cierra el EntityManager del hilo actual
     */
    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if (em != null) {
            try {
                if (em.isOpen()) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    em.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("No se pudo cerrar la conexión al EntityManager", e);
            } finally {
                threadLocal.remove();
            }
        }
    }

    /**
     * metodo que inicia una transacción si no hay una activa
     */
    public static void beginTransaction() {
        EntityManager em = getEntityManager();
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    /**
     * metodo que hace commit de la transacción activa
     */
    public static void commitTransaction() {
        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    /**
     * metodo que hace rollback de la transacción activa
     */
    public static void rollbackTransaction() {
        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    /**
     * metodo que cierra el EntityManagerFactory al finalizar el programa
     */
    public static void shutdown() {
        try {
            closeEntityManager();
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cerrar la conexión a la base de datos", e);
        }
    }
}
