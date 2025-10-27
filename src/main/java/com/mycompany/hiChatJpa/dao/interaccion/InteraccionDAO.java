package com.mycompany.hiChatJpa.dao.interaccion;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupular las interacciones
 * @author gatog
 */
public class InteraccionDAO implements IInteraccionDAO {

    /**
     * metodo que permite insertar una interaccion
     * @param interaccion 
     */
    @Override
    public void insertar(Interaccion interaccion) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(interaccion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite actualaizar una interaccion
     * @param interaccion 
     */
    @Override
    public void actualizar(Interaccion interaccion) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(interaccion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite eliminar una interaccion
     * @param id 
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Interaccion interaccion = em.find(Interaccion.class, id);
            if (interaccion != null) em.remove(interaccion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consular una interaccion por id
     * @param id
     * @return 
     */
    @Override
    public Interaccion buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Interaccion.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar todas las interacciones
     * @return 
     */
    @Override
    public List<Interaccion> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findAll", Interaccion.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar las interacciones por emisor
     * @param usuario
     * @return 
     */
    @Override
    public List<Interaccion> buscarPorEmisor(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findByEmisor", Interaccion.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar las interacciones por receptor
     * @param usuario
     * @return 
     */
    @Override
    public List<Interaccion> buscarPorReceptor(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findByReceptor", Interaccion.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar las interacciones por el tipo de interaccion
     * @param tipo
     * @return 
     */
    @Override
    public List<Interaccion> buscarPorTipo(TipoInteraccion tipo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findByTipo", Interaccion.class);
            query.setParameter("tipo", tipo);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}