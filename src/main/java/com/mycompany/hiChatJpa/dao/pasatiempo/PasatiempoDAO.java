package com.mycompany.hiChatJpa.dao.pasatiempo;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupular los pasatiempos
 * @author gatog
 */
public class PasatiempoDAO implements IPasatiempoDAO {

    /**
     * metodo que permite agregar un pasatiempo
     * @param pasatiempo 
     */
    @Override
    public void insertar(Pasatiempo pasatiempo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pasatiempo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite actualizar un pasatiempo
     * @param pasatiempo 
     */
    @Override
    public void actualizar(Pasatiempo pasatiempo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pasatiempo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite eliminar un pasatiempo
     * @param id 
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Pasatiempo p = em.find(Pasatiempo.class, id);
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar un pasatiempo por id
     * @param id
     * @return 
     */
    @Override
    public Pasatiempo buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Pasatiempo.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar todos los pasatiempos
     * @return 
     */
    @Override
    public List<Pasatiempo> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Pasatiempo> query = em.createNamedQuery("Pasatiempo.findAll", Pasatiempo.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar los pasatiempos por nombre
     * @param nombre
     * @return 
     */
    @Override
    public Pasatiempo buscarPorNombre(String nombre) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Pasatiempo> query = em.createNamedQuery("Pasatiempo.findByNombre", Pasatiempo.class);
            query.setParameter("nombre", nombre);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}
