package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IInteraccionRepository;

/**
 * clase que permite manupular las interacciones
 * @author gatog
 */
public class InteraccionRepository implements IInteraccionRepository {
    
    private final EntityManager entityManager;
    
    public InteraccionRepository(EntityManager em) {
        this.entityManager = em;
    }
    
    /**
     * Inserta una nueva interacción en la base de datos.
     * 
     * @param interaccion Interacción a insertar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Interaccion insertar(Interaccion interaccion) throws RepositoryException {
        try {
            entityManager.persist(interaccion);
            return interaccion;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar la interacción", e);
        }
    }

    /**
     * Actualiza una interacción existente.
     * 
     * @param interaccion Interacción con los datos actualizados
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Interaccion actualizar(Interaccion interaccion) throws RepositoryException {
        try {
            return entityManager.merge(interaccion);
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar la interacción", e);
        }
    }

    /**
     * Elimina una interacción por su ID.
     * 
     * @param id ID de la interacción a eliminar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Interaccion eliminar(Long id) throws RepositoryException {
        try {
            Interaccion interaccion = entityManager.find(Interaccion.class, id);
            if (interaccion == null) {
                throw new EntityNotFoundException("no se pudo encontrar la interaccion");
            }
            
            entityManager.remove(interaccion);
            return interaccion;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar la interacción", e);
        }
    }

    /**
     * Busca una interacción por su ID.
     * 
     * @param id ID de la interacción
     * @return Interacción encontrada o null si no existe
     */
    @Override
    public Interaccion buscar(Long id) throws RepositoryException {
        try {
            return entityManager.find(Interaccion.class, id);
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar la interacción", e);
        }
    }

    /**
     * Lista todas las interacciones (máximo 100 registros).
     * 
     * @param limit
     * @param offset
     * @return Lista de interacciones
     */
    @Override
    public List<Interaccion> listar(int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findAll", Interaccion.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de interacciones", e);
        }
    }

    /**
     * Busca las interacciones enviadas por un usuario (emisor).
     * 
     * @param usuario Usuario emisor
     * @param limit
     * @param offset
     * @return Lista de interacciones enviadas
     */
    @Override
    public List<Interaccion> buscarPorEmisor(Usuario usuario, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findByEmisor", Interaccion.class);
            query.setParameter("emisor", usuario);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorEmisor", "No se pudieron buscar las interacciones por emisor", e);
        }
    }

    /**
     * Busca las interacciones recibidas por un usuario (receptor).
     * 
     * @param usuario Usuario receptor
     * @param limit
     * @param offset
     * @return Lista de interacciones recibidas
     */
    @Override
    public List<Interaccion> buscarPorReceptor(Usuario usuario, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findByReceptor", Interaccion.class);
            query.setParameter("receptor", usuario);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorReceptor","No se pudieron buscar las interacciones por receptor", e);
        }
    }

    /**
     * Busca las interacciones por tipo.
     * 
     * @param tipo Tipo de interacción a filtrar
     * @param limit
     * @param offset
     * @return Lista de interacciones del tipo especificado
     */
    @Override
    public List<Interaccion> buscarPorTipo(TipoInteraccion tipo, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findByTipo", Interaccion.class);
            query.setParameter("tipo", tipo);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorTipo", "No se pudieron buscar las interacciones por tipo", e);
        }
    }
}
