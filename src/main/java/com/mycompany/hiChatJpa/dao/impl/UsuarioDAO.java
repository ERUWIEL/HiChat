package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.dao.IUsuarioDAO;
import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupular los usuarios
 * @author gatog
 */
public class UsuarioDAO implements IUsuarioDAO {

    /**
     * metodo que permite agregar un usuario
     * @param usuario 
     */
    @Override
    public void insertar(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite actualizar un usuario
     * @param usuario 
     */
    @Override
    public void actualizar(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite eliminar un usuario
     * @param id 
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            if (u != null) em.remove(u);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar un usuairo por id
     * @param id
     * @return 
     */
    @Override
    public Usuario buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar todos los usuarios
     * @return 
     */
    @Override
    public List<Usuario> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findAll", Usuario.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar a un usuario por su correo
     * @param correo
     * @return 
     */
    @Override
    public Usuario buscarPorCorreo(String correo) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByCorreo", Usuario.class);
            query.setParameter("correo", correo);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar a un usuario por su nombre completo
     * @param nombre
     * @param apellidoPaterno
     * @return 
     */
    @Override
    public List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByNombreCompleto", Usuario.class);
            query.setParameter("nombre", nombre);
            query.setParameter("apellidoPaterno", apellidoPaterno);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
