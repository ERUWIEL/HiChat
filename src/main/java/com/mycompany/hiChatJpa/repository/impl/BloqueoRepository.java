package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IBloqueoRepository;

/**
 * clase dao que permite manipular los bloqueos
 *
 * @author gatog
 */
public class BloqueoRepository implements IBloqueoRepository {
    private static final int MAX_RESULTS = 100;
    private final EntityManager entityManager;
    
    public BloqueoRepository(EntityManager em){
        this.entityManager = em;
    }
    
    /**
     * Inserta un nuevo bloqueo en la base de datos.
     * 
     * @param b Bloqueo a insertar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean insertar(Bloqueo b) {
        try {
            entityManager.persist(b);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el bloqueo", e);
        }
    }

    /**
     * Actualiza un bloqueo existente.
     * 
     * @param e Bloqueo con los datos actualizados
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean actualizar(Bloqueo e) {
        try {
            entityManager.merge(e);
            return true;
        } catch (Exception ex) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el bloqueo", ex);
        }
    }

    /**
     * Elimina un bloqueo por su ID.
     * 
     * @param id ID del bloqueo a eliminar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean eliminar(Long id) {
        try {
            Bloqueo bloqueo = entityManager.find(Bloqueo.class, id);
            if (bloqueo != null) {
                entityManager.remove(bloqueo);
                return true;
            } else {
                throw new EntityNotFoundException("no se encontro el bloqueo");
            }
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el bloqueo", e);
        }
    }

    /**
     * Busca un bloqueo por su ID.
     * 
     * @param id ID del bloqueo
     * @return Bloqueo encontrado o null si no existe
     */
    @Override
    public Bloqueo buscar(Long id) {
        try {
            Bloqueo bloqueo = entityManager.find(Bloqueo.class, id);
            return bloqueo;
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el bloqueo", e);
        }
    }

    /**
     * Lista todos los bloqueos (máximo 100 registros).
     * 
     * @return Lista de bloqueos
     */
    @Override
    public List<Bloqueo> listar() {
        try {
            TypedQuery<Bloqueo> query = entityManager.createNamedQuery("Bloqueo.findAll", Bloqueo.class);
            query.setMaxResults(MAX_RESULTS);
            List<Bloqueo> bloqueos = query.getResultList();
            return bloqueos;
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo listar los bloqueos", e);
        }
    }

    /**
     * Busca los bloqueos realizados por un usuario.
     * 
     * @param usuario Usuario bloqueador
     * @return Lista de bloqueos hechos por el usuario
     */
    @Override
    public List<Bloqueo> buscarPorBloqueador(Usuario usuario) {
        try {
            TypedQuery<Bloqueo> query = entityManager.createNamedQuery("Bloqueo.findByBloqueador", Bloqueo.class);
            query.setParameter("bloqueador", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Bloqueo> bloqueos = query.getResultList();
            return bloqueos;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorBloqueador","No se pudieron buscar los bloqueos por bloqueador", e);
        }
    }

    /**
     * Busca los bloqueos donde un usuario fue bloqueado.
     * 
     * @param usuario Usuario bloqueado
     * @return Lista de bloqueos en los que el usuario fue bloqueado
     */
    @Override
    public List<Bloqueo> buscarPorBloqueado(Usuario usuario) {
        try {
            TypedQuery<Bloqueo> query = entityManager.createNamedQuery("Bloqueo.findByBloqueado", Bloqueo.class);
            query.setParameter("bloqueado", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Bloqueo> bloqueos = query.getResultList();
            return bloqueos;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorBloqueado","No se pudieron buscar los bloqueos por bloqueado", e);
        }
    }
}
