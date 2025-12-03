package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IMensajeDAO;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupilar los mensajes
 * @author gatog
 */
public class MensajeDAO implements IMensajeDAO {

    private static final int MAX_RESULTS = 100;

    /**
     * Inserta un nuevo mensaje en la base de datos.
     * 
     * @param mensaje Mensaje a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Mensaje mensaje) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(mensaje);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("insertar", "No se pudo insertar el mensaje", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Actualiza un mensaje existente.
     * 
     * @param mensaje Mensaje con los datos actualizados
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Mensaje mensaje) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(mensaje);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("actualizar", "No se pudo actualizar el mensaje", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Elimina un mensaje por su ID.
     * 
     * @param id ID del mensaje a eliminar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Mensaje mensaje = em.find(Mensaje.class, id);
            if (mensaje != null) {
                em.remove(mensaje);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("eliminar", "No se pudo eliminar el mensaje", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca un mensaje por su ID.
     * 
     * @param id ID del mensaje
     * @return Mensaje encontrado o null si no existe
     */
    @Override
    public Mensaje buscar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Mensaje mensaje = em.find(Mensaje.class, id);

            return mensaje;

        } catch (Exception e) {
            throw new PersistenceException("buscar", "No se pudo buscar el mensaje", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Lista todos los mensajes (máximo 100 registros).
     * 
     * @return Lista de mensajes
     */
    @Override
    public List<Mensaje> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Mensaje> query = em.createNamedQuery("Mensaje.findAll", Mensaje.class);
            query.setMaxResults(MAX_RESULTS);

            List<Mensaje> mensajes = query.getResultList();

            return mensajes;

        } catch (Exception e) {
            throw new PersistenceException("listar", "No se pudo obtener la lista de mensajes", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca todos los mensajes pertenecientes a un chat específico.
     * 
     * @param chat Chat del cual se desean obtener los mensajes
     * @return Lista de mensajes asociados al chat
     */
    @Override
    public List<Mensaje> buscarPorChat(Chat chat) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Mensaje> query = em.createNamedQuery("Mensaje.findByChat", Mensaje.class);
            query.setParameter("chat", chat);
            query.setMaxResults(MAX_RESULTS);

            List<Mensaje> mensajes = query.getResultList();

            return mensajes;

        } catch (Exception e) {
            throw new PersistenceException("buscarPorChat", "No se pudieron buscar los mensajes del chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca los mensajes no vistos por un usuario determinado.
     * 
     * @param usuario Usuario para el cual se desean obtener los mensajes no vistos
     * @return Lista de mensajes no vistos
     */
    @Override
    public List<Mensaje> buscarNoVistosPorUsuario(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Mensaje> query = em.createNamedQuery("Mensaje.findNoVistosPorUsuario", Mensaje.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(MAX_RESULTS);

            List<Mensaje> mensajes = query.getResultList();

            return mensajes;

        } catch (Exception e) {
            throw new PersistenceException("buscarNoVistosPorUsuario", "No se pudieron buscar los mensajes no vistos del usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}

