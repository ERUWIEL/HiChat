package com.mycompany.hiChatJpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * clase que nos permitira usar el patron singleton implementando logs de
 * control para auditoriar su funcionamiento
 *
 * @author gatog
 */
public class JpaUtil {

    private static final Logger logger = LoggerFactory.getLogger(JpaUtil.class);
    private static final String PERSISTENCE_UNIT = "HiChatPU";

    private static EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    private JpaUtil() {
    }

    /**
     * obtiene el EntityManagerFactory Thread-safe con double-check locking
     *
     * @return EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (JpaUtil.class) {
                if (emf == null) {
                    try {
                        logger.info("Inicializando EntityManagerFactory...");
                        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                        logger.info("EntityManagerFactory inicializado correctamente");
                    } catch (Exception e) {
                        logger.error("Error al inicializar EntityManagerFactory", e);
                        throw new RuntimeException("No se pudo inicializar la conexión a la base de datos", e);
                    }
                }
            }
        }
        return emf;
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
            logger.debug("EntityManager creado para el hilo: {}", Thread.currentThread().getName());
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
                        logger.warn("Se hizo rollback de una transacción activa al cerrar EntityManager");
                    }
                    em.close();
                    logger.debug("EntityManager cerrado para el hilo: {}", Thread.currentThread().getName());
                }
            } catch (Exception e) {
                logger.error("Error al cerrar EntityManager", e);
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
            logger.debug("Transacción iniciada");
        }
    }

    /**
     * metodo que hace commit de la transacción activa
     */
    public static void commitTransaction() {
        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
            logger.debug("Transacción confirmada");
        }
    }

    /**
     * metodo que hace rollback de la transacción activa
     */
    public static void rollbackTransaction() {
        EntityManager em = getEntityManager();
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
            logger.debug("Transacción revertida");
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
                logger.info("EntityManagerFactory cerrado");
            }
        } catch (Exception e) {
            logger.error("Error al cerrar EntityManagerFactory", e);
        }
    }
}
