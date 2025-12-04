package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IChatRepository;

/**
 * clase dao que permite manupular los chats
 *
 * @author gatog
 */
public class ChatRepository implements IChatRepository {

    private static final int MAX_RESULTS = 100;
    private final EntityManager entityManager;

    public ChatRepository(EntityManager em) {
        this.entityManager = em;
    }

    /**
     * Inserta un nuevo chat en la base de datos.
     *
     * @param chat Chat a insertar
     * @return
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean insertar(Chat chat) {
        try {
            entityManager.persist(chat);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el chat", e);
        }
    }

    /**
     * Actualiza un chat existente.
     *
     * @param chat Chat con los datos actualizados
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean actualizar(Chat chat) {
        try {
            entityManager.merge(chat);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el chat", e);
        }
    }

    /**
     * Elimina un chat por su ID.
     *
     * @param id ID del chat a eliminar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean eliminar(Long id) {
        try {
            Chat chat = entityManager.find(Chat.class, id);
            if (chat != null) {
                entityManager.remove(chat);
                return true;
            } else {
                throw new EntityNotFoundException("no se encontro el chat");
            }
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el chat", e);
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
        try {
            Chat chat = entityManager.find(Chat.class, id);
            return chat;
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el chat", e);
        }
    }

    /**
     * Lista todos los chats (máximo 100 registros).
     *
     * @return Lista de chats
     */
    @Override
    public List<Chat> listar() {
        try {
            TypedQuery<Chat> query = entityManager.createNamedQuery("Chat.findAll", Chat.class);
            query.setMaxResults(MAX_RESULTS);
            List<Chat> chats = query.getResultList();
            return chats;
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo listar los chats", e);
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
        try {
            TypedQuery<Chat> query = entityManager.createNamedQuery("Chat.findByNombre", Chat.class);
            query.setParameter("nombre", "%" + nombre + "%");
            query.setMaxResults(MAX_RESULTS);
            List<Chat> chats = query.getResultList();
            return chats;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorNombre", "No se pudieron buscar los chats por nombre", e);
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
        try {
            TypedQuery<Chat> query = entityManager.createNamedQuery("Chat.findByParticipante", Chat.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Chat> chats = query.getResultList();
            return chats;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorParticipante", "No se pudieron buscar los chats por participante", e);
        }
    }
}
