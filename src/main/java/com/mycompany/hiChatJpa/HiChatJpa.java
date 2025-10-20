
package com.mycompany.hiChatJpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Laboratorios
 */
public class HiChatJpa {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HiChatPU");
        EntityManager em = emf.createEntityManager();
    }
}
