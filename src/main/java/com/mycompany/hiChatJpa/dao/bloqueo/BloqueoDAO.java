package com.mycompany.hiChatJpa.dao.bloqueo;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase dao que permite manipular los bloqueos
 *
 * @author gatog
 */
public class BloqueoDAO implements IBloqueoDAO {

    /**
     * metodo que permite agregar un bloqueo
     *
     * @param b
     */
    @Override
    public void insertar(Bloqueo b) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(b);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite modificar un bloqueo
     *
     * @param b
     */
    @Override
    public void actualizar(Bloqueo b) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(b);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite eliminar un bloqueo
     *
     * @param id
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Bloqueo bloqueo = em.find(Bloqueo.class, id);
            if (bloqueo != null) {
                em.remove(bloqueo);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar un bloqueo por id
     *
     * @param id
     * @return
     */
    @Override
    public Bloqueo buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Bloqueo.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar todos los bloqueos
     *
     * @return
     */
    @Override
    public List<Bloqueo> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Bloqueo> query = em.createNamedQuery("Bloqueo.findAll", Bloqueo.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar los bloqueos que ha hecho un usuario
     *
     * @param usuario
     * @return
     */
    @Override
    public List<Bloqueo> buscarPorBloqueador(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Bloqueo> query = em.createNamedQuery("Bloqueo.findByBloqueador", Bloqueo.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar los bloqueos que tiene un usuario
     * @param usuario
     * @return 
     */
    @Override
    public List<Bloqueo> buscarPorBloqueado(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Bloqueo> query = em.createNamedQuery("Bloqueo.findByBloqueado", Bloqueo.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
