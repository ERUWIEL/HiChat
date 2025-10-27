package com.mycompany.hiChatJpa.dao.match;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupular los matches
 * @author gatog
 */
public class MatchDAO implements IMatchDAO {

    /**
     * metodo que permite insertar un match
     * @param match 
     */
    @Override
    public void insertar(Match match) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(match);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite acualizar un match
     * @param match 
     */
    @Override
    public void actualizar(Match match) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(match);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite eliminar un match
     * @param id 
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Match match = em.find(Match.class, id);
            if (match != null) em.remove(match);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consutlar un match por id
     * @param id
     * @return 
     */
    @Override
    public Match buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Match.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar todos los matches
     * @return 
     */
    @Override
    public List<Match> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Match> query = em.createNamedQuery("Match.findAll", Match.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar los matches por su usuario a
     * @param usuario
     * @return 
     */
    @Override
    public List<Match> buscarPorUsuarioA(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Match> query = em.createNamedQuery("Match.findByUsuarioA", Match.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar los matches por su usuario b
     * @param usuario
     * @return 
     */
    @Override
    public List<Match> buscarPorUsuarioB(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Match> query = em.createNamedQuery("Match.findByUsuarioB", Match.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}