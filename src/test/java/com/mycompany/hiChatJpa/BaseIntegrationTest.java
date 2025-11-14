
package com.mycompany.hiChatJpa;

import com.mycompany.hiChatJpa.config.JpaUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * 
 * @author gatog
 */
public abstract class BaseIntegrationTest {
    
    protected EntityManager em;
    
    @BeforeAll
    public static void setUpClass() {
        JpaUtil.getEntityManagerFactory();
    }
    
    @BeforeEach
    public void setUp() {
        em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
    }
    
    @AfterEach
    public void tearDown() {
        if (em != null) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JpaUtil.closeEntityManager();
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
        JpaUtil.shutdown();
    }
    
    protected void flush() {
        em.flush();
    }
    
    protected void clear() {
        em.clear();
    }
    
    protected void flushAndClear() {
        em.flush();
        em.clear();
    }
}
