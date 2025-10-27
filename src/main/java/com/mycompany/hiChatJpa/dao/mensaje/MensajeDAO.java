package com.mycompany.hiChatJpa.dao.mensaje;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupilar los mensajes
 * @author gatog
 */
public class MensajeDAO implements IMensajeDAO {

    /**
     * metodo que permite insertar un mensaje
     * @param mensaje 
     */
    @Override
    public void insertar(Mensaje mensaje) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(mensaje);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite actualizar un mensaje
     * @param mensaje 
     */
    @Override
    public void actualizar(Mensaje mensaje) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(mensaje);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite eliminar un mensaje
     * @param id 
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Mensaje mensaje = em.find(Mensaje.class, id);
            if (mensaje != null) em.remove(mensaje);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar por id un mensaje
     * @param id
     * @return 
     */
    @Override
    public Mensaje buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Mensaje.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar todos los mensajes
     * @return 
     */
    @Override
    public List<Mensaje> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Mensaje> query = em.createNamedQuery("Mensaje.findAll", Mensaje.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar los mensajes por chat
     * @param chat
     * @return 
     */
    @Override
    public List<Mensaje> buscarPorChat(Chat chat) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Mensaje> query = em.createNamedQuery("Mensaje.findByChat", Mensaje.class);
            query.setParameter("chat", chat);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar los mensajes no vistos por un usuario
     * @param usuario
     * @return 
     */
    @Override
    public List<Mensaje> buscarNoVistosPorUsuario(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Mensaje> query = em.createNamedQuery("Mensaje.findNoVistosByUsuario", Mensaje.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
