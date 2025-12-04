package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IMensajeRepository;

/**
 * clase que permite manupilar los mensajes
 * @author gatog
 */
public class MensajeRepository implements IMensajeRepository {

    private static final int MAX_RESULTS = 100;
    private final EntityManager entityManager;
    
    public MensajeRepository(EntityManager em){
        this.entityManager = em;
    }
    
    
    /**
     * Inserta un nuevo mensaje en la base de datos.
     * 
     * @param mensaje Mensaje a insertar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean insertar(Mensaje mensaje) {
        try {
            entityManager.persist(mensaje);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el mensaje", e);
        }
    }

    /**
     * Actualiza un mensaje existente.
     * 
     * @param mensaje Mensaje con los datos actualizados
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean actualizar(Mensaje mensaje) {
        try {
            entityManager.merge(mensaje);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el mensaje", e);
        }
    }

    /**
     * Elimina un mensaje por su ID.
     * 
     * @param id ID del mensaje a eliminar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean eliminar(Long id) {
        try {
            Mensaje mensaje = entityManager.find(Mensaje.class, id);
            if (mensaje != null) {
                entityManager.remove(mensaje);
                return true;
            } else {
                throw new EntityNotFoundException("no se encontro el mensaje");
            }
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el mensaje", e);
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
        try {
            Mensaje mensaje = entityManager.find(Mensaje.class, id);
            return mensaje;
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el mensaje", e);
        }
    }

    /**
     * Lista todos los mensajes (máximo 100 registros).
     * 
     * @return Lista de mensajes
     */
    @Override
    public List<Mensaje> listar() {
        try {
            TypedQuery<Mensaje> query = entityManager.createNamedQuery("Mensaje.findAll", Mensaje.class);
            query.setMaxResults(MAX_RESULTS);
            List<Mensaje> mensajes = query.getResultList();
            return mensajes;
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de mensajes", e);
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
        try {
            TypedQuery<Mensaje> query = entityManager.createNamedQuery("Mensaje.findByChat", Mensaje.class);
            query.setParameter("chat", chat);
            query.setMaxResults(MAX_RESULTS);
            List<Mensaje> mensajes = query.getResultList();
            return mensajes;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorChat", "No se pudieron buscar los mensajes del chat", e);
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
        try {
            TypedQuery<Mensaje> query = entityManager.createNamedQuery("Mensaje.findNoVistosPorUsuario", Mensaje.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Mensaje> mensajes = query.getResultList();
            return mensajes;
        } catch (Exception e) {
            throw new RepositoryException("buscarNoVistosPorUsuario", "No se pudieron buscar los mensajes no vistos del usuario", e);
        }
    }
}

