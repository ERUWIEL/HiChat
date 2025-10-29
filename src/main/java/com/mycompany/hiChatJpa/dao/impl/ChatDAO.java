package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IChatDAO;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * clase dao que permite manupular los chats
 * @author gatog
 */
public class ChatDAO implements IChatDAO {

    private static final Logger logger = LoggerFactory.getLogger(ChatDAO.class);
    private static final int MAX_RESULTS = 100;

    /**
     * Inserta un nuevo chat en la base de datos.
     * 
     * @param chat Chat a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Chat chat) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(chat);

            em.getTransaction().commit();
            logger.info("Chat insertado correctamente: {}", chat.getNombre());

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en inserción de chat", e);
            }
            logger.error("Error al insertar chat", e);
            throw new PersistenceException("insertar", "No se pudo insertar el chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Actualiza un chat existente.
     * 
     * @param chat Chat con los datos actualizados
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Chat chat) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(chat);

            em.getTransaction().commit();
            logger.info("Chat actualizado correctamente: ID {}", chat.getIdChat());

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en actualización de chat", e);
            }
            logger.error("Error al actualizar chat", e);
            throw new PersistenceException("actualizar", "No se pudo actualizar el chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Elimina un chat por su ID.
     * 
     * @param id ID del chat a eliminar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Chat chat = em.find(Chat.class, id);
            if (chat != null) {
                em.remove(chat);
                logger.info("Chat eliminado correctamente: ID {}", id);
            } else {
                logger.warn("Intento de eliminar chat inexistente: ID {}", id);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en eliminación de chat", e);
            }
            logger.error("Error al eliminar chat con ID: {}", id, e);
            throw new PersistenceException("eliminar", "No se pudo eliminar el chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca un chat por su ID.
     * 
     * @param id ID del chat
     * @return Chat encontrado o null si no existe
     */
    @Override
    public Chat buscar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Chat chat = em.find(Chat.class, id);

            if (chat != null) {
                logger.debug("Chat encontrado: ID {}", id);
            } else {
                logger.debug("Chat no encontrado: ID {}", id);
            }

            return chat;

        } catch (Exception e) {
            logger.error("Error al buscar chat por ID: {}", id, e);
            throw new PersistenceException("buscar", "No se pudo buscar el chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Lista todos los chats (máximo 100 registros).
     * 
     * @return Lista de chats
     */
    @Override
    public List<Chat> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Chat> query = em.createNamedQuery("Chat.findAll", Chat.class);
            query.setMaxResults(MAX_RESULTS);

            List<Chat> chats = query.getResultList();
            logger.debug("Se listaron {} chats", chats.size());

            return chats;

        } catch (Exception e) {
            logger.error("Error al listar chats", e);
            throw new PersistenceException("listar", "No se pudo listar los chats", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca chats por nombre (o parte del nombre).
     * 
     * @param nombre Nombre del chat o fragmento
     * @return Lista de chats que coinciden con el nombre
     */
    @Override
    public List<Chat> buscarPorNombre(String nombre) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Chat> query = em.createNamedQuery("Chat.findByNombre", Chat.class);
            query.setParameter("nombre", "%" + nombre + "%");
            query.setMaxResults(MAX_RESULTS);

            List<Chat> chats = query.getResultList();
            logger.debug("Se encontraron {} chats con nombre similar a '{}'", chats.size(), nombre);

            return chats;

        } catch (Exception e) {
            logger.error("Error al buscar chats por nombre '{}'", nombre, e);
            throw new PersistenceException("buscarPorNombre", 
                                           "No se pudieron buscar los chats por nombre", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca los chats en los que participa un usuario.
     * 
     * @param usuario Usuario participante
     * @return Lista de chats donde el usuario participa
     */
    @Override
    public List<Chat> buscarPorParticipante(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Chat> query = em.createNamedQuery("Chat.findByParticipante", Chat.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(MAX_RESULTS);

            List<Chat> chats = query.getResultList();
            logger.debug("Se encontraron {} chats del usuario ID {}", chats.size(), usuario.getIdUsuario());

            return chats;

        } catch (Exception e) {
            logger.error("Error al buscar chats por participante ID: {}", usuario.getIdUsuario(), e);
            throw new PersistenceException("buscarPorParticipante", 
                                           "No se pudieron buscar los chats por participante", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
