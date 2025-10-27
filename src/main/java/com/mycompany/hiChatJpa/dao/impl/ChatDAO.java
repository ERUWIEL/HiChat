package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IChatDAO;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase dao que permite manupular los chats
 * @author gatog
 */
public class ChatDAO implements IChatDAO {

    /**
     * metodo que permite agregar un chat
     * @param chat 
     */
    @Override
    public void insertar(Chat chat) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(chat);
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
     * metodo que permite actualizar un chat
     * @param chat 
     */
    @Override
    public void actualizar(Chat chat) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(chat);
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
     * metodo que permite eliminar un chat
     * @param id 
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Chat chat = em.find(Chat.class, id);
            if (chat != null) {
                em.remove(chat);
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
     * metodo que permite buscar un chat por id
     * @param id
     * @return 
     */
    @Override
    public Chat buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Chat.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar todos los chats
     * @return 
     */
    @Override
    public List<Chat> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Chat> query = em.createNamedQuery("Chat.findAll", Chat.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar un chat por su nombre
     * @param nombre
     * @return 
     */
    @Override
    public List<Chat> buscarPorNombre(String nombre) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Chat> query = em.createNamedQuery("Chat.findByNombre", Chat.class);
            query.setParameter("nombre", "%" + nombre + "%");
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscar un chat por el nombre de su participante
     * @param usuario
     * @return 
     */
    @Override
    public List<Chat> buscarPorParticipante(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Chat> query = em.createNamedQuery("Chat.findByParticipante", Chat.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
