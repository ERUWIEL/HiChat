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

public class MensajeRepository implements IMensajeRepository {

    private final EntityManager entityManager;

    public MensajeRepository(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public Mensaje insertar(Mensaje mensaje) throws RepositoryException {
        try {
            entityManager.persist(mensaje);
            entityManager.flush();
            return mensaje;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el mensaje", e);
        }
    }

    @Override
    public Mensaje actualizar(Mensaje mensaje) throws RepositoryException {
        try {
            return entityManager.merge(mensaje);
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el mensaje", e);
        }
    }

    @Override
    public Mensaje eliminar(Long id) throws RepositoryException {
        try {
            Mensaje mensaje = entityManager.find(Mensaje.class, id);
            if (mensaje == null) {
                throw new EntityNotFoundException("No se encontró el mensaje");
            }

            entityManager.remove(mensaje);
            return mensaje;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el mensaje", e);
        }
    }

    @Override
    public Mensaje buscar(Long id) throws RepositoryException {
        try {
            return entityManager.find(Mensaje.class, id);
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el mensaje", e);
        }
    }

    @Override
    public List<Mensaje> listar(int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Mensaje> query = entityManager.createNamedQuery("Mensaje.findAll", Mensaje.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de mensajes", e);
        }
    }

    @Override
    public List<Mensaje> buscarPorChat(Chat chat, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Mensaje> query = entityManager.createNamedQuery("Mensaje.findByChat", Mensaje.class);
            query.setParameter("chat", chat);
            query.setMaxResults(limit);
            query.setFirstResult(offset);

            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorChat", "No se pudieron buscar los mensajes del chat", e);
        }
    }

    @Override
    public List<Mensaje> buscarNoVistosPorUsuario(Usuario usuario, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Mensaje> query = entityManager.createNamedQuery("Mensaje.findNoVistosPorUsuario", Mensaje.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            query.setParameter("usuario", usuario);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarNoVistosPorUsuario", "No se pudieron buscar los mensajes no vistos", e);
        }
    }
}
