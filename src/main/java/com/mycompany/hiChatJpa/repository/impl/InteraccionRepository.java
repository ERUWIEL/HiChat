package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
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
    
    private static final int MAX_RESULTS = 100;
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
    public boolean insertar(Interaccion interaccion) {
        try {
            entityManager.persist(interaccion);
            return true;
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
    public boolean actualizar(Interaccion interaccion) {
        try {
            entityManager.merge(interaccion);
            return true;
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
    public boolean eliminar(Long id) {
        try {
            Interaccion interaccion = entityManager.find(Interaccion.class, id);
            if (interaccion != null) {
                entityManager.remove(interaccion);
                return true;
            } else {
                throw new EntityNotFoundException("no se pudo encontrar la interaccion");
            }
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
    public Interaccion buscar(Long id) {
        try {
            Interaccion interaccion = entityManager.find(Interaccion.class, id);
            return interaccion;
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar la interacción", e);
        }
    }

    /**
     * Lista todas las interacciones (máximo 100 registros).
     * 
     * @return Lista de interacciones
     */
    @Override
    public List<Interaccion> listar() {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findAll", Interaccion.class);
            query.setMaxResults(MAX_RESULTS);
            List<Interaccion> interacciones = query.getResultList();
            return interacciones;
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de interacciones", e);
        }
    }

    /**
     * Busca las interacciones enviadas por un usuario (emisor).
     * 
     * @param usuario Usuario emisor
     * @return Lista de interacciones enviadas
     */
    @Override
    public List<Interaccion> buscarPorEmisor(Usuario usuario) {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findByEmisor", Interaccion.class);
            query.setParameter("emisor", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Interaccion> interacciones = query.getResultList();
            return interacciones;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorEmisor", "No se pudieron buscar las interacciones por emisor", e);
        }
    }

    /**
     * Busca las interacciones recibidas por un usuario (receptor).
     * 
     * @param usuario Usuario receptor
     * @return Lista de interacciones recibidas
     */
    @Override
    public List<Interaccion> buscarPorReceptor(Usuario usuario) {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findByReceptor", Interaccion.class);
            query.setParameter("receptor", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Interaccion> interacciones = query.getResultList();
            return interacciones;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorReceptor","No se pudieron buscar las interacciones por receptor", e);
        }
    }

    /**
     * Busca las interacciones por tipo.
     * 
     * @param tipo Tipo de interacción a filtrar
     * @return Lista de interacciones del tipo especificado
     */
    @Override
    public List<Interaccion> buscarPorTipo(TipoInteraccion tipo) {
        try {
            TypedQuery<Interaccion> query = entityManager.createNamedQuery("Interaccion.findByTipo", Interaccion.class);
            query.setParameter("tipo", tipo);
            query.setMaxResults(MAX_RESULTS);
            List<Interaccion> interacciones = query.getResultList();
            return interacciones;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorTipo", "No se pudieron buscar las interacciones por tipo", e);
        }
    }
}
